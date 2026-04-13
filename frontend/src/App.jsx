import { useEffect, useRef, useState } from "react";

const initialState = {
  counts: { alerts: 0, maintenanceOrders: 0, machines: 0 },
  alerts: [],
  maintenanceOrders: [],
  machines: [],
};

function sortAlerts(list) {
  return [...list].sort((a, b) => new Date(b.horodatage) - new Date(a.horodatage));
}

function sortOrders(list) {
  return [...list].sort((a, b) => new Date(b.dateOuverture) - new Date(a.dateOuverture));
}

function sortMachines(list) {
  return [...list].sort((a, b) => a.nom.localeCompare(b.nom));
}

function upsertById(list, item) {
  const index = list.findIndex((entry) => entry.uuid === item.uuid);
  if (index === -1) {
    return [item, ...list];
  }

  const next = [...list];
  next[index] = item;
  return next;
}

export default function App() {
  const [snapshot, setSnapshot] = useState(initialState);
  const [status, setStatus] = useState("Connecting...");
  const [events, setEvents] = useState([]);
  const sourceRef = useRef(null);

  useEffect(() => {
    let cancelled = false;

    async function loadSnapshot() {
      const response = await fetch("/api/dashboard");
      const data = await response.json();
      if (!cancelled) {
        setSnapshot(data);
        setStatus("Snapshot loaded");
      }
    }

    loadSnapshot().catch((error) => {
      if (!cancelled) {
        setStatus(`Snapshot error: ${error.message}`);
      }
    });

    const source = new EventSource("/api/dashboard/stream");
    sourceRef.current = source;

    source.addEventListener("connected", () => {
      setStatus("Live stream connected");
    });

    source.addEventListener("projection-update", (event) => {
      const update = JSON.parse(event.data);

      setEvents((current) => [update, ...current].slice(0, 12));
      setSnapshot((current) => {
        const next = { ...current };

        if (update.domain === "alerts") {
          const alerts = sortAlerts(upsertById(current.alerts, update.payload));
          next.alerts = alerts.slice(0, 12);
          next.counts = { ...current.counts, alerts: alerts.length };
        }

        if (update.domain === "maintenanceOrders") {
          const maintenanceOrders = sortOrders(upsertById(current.maintenanceOrders, update.payload));
          next.maintenanceOrders = maintenanceOrders.slice(0, 12);
          next.counts = { ...next.counts, maintenanceOrders: maintenanceOrders.length };
        }

        if (update.domain === "machines") {
          const machines = sortMachines(upsertById(current.machines, update.payload));
          next.machines = machines;
          next.counts = { ...next.counts, machines: machines.length };
        }

        return next;
      });
    });

    source.onerror = () => {
      setStatus("Stream disconnected, waiting for reconnect...");
    };

    return () => {
      cancelled = true;
      source.close();
    };
  }, []);

  return (
    <main className="page-shell">
      <section className="hero">
        <p className="eyebrow">{"Factory -> Kafka -> demoConsumer -> SSE -> React"}</p>
        <h1>Factory Live Operations Board</h1>
        <p className="intro">
          This UI only shows data projected from <strong>factory</strong>. Kafka triggers the updates,
          <strong> demoConsumer</strong> refreshes its local read model, then pushes the frontend through SSE.
        </p>
        <div className="status-pill">{status}</div>
      </section>

      <section className="stats-grid">
        <StatCard label="Alerts projected" value={snapshot.counts.alerts} accent="red" />
        <StatCard label="Maintenance orders" value={snapshot.counts.maintenanceOrders} accent="amber" />
        <StatCard label="Machines tracked" value={snapshot.counts.machines} accent="teal" />
      </section>

      <section className="board">
        <Panel
          title="Latest alerts"
          subtitle="Thin event -> callback to factory -> local projection"
          items={snapshot.alerts}
          renderItem={(alert) => (
            <article className="item-card critical" key={alert.uuid}>
              <header>
                <strong>{alert.typeDepassement}</strong>
                <span>{new Date(alert.horodatage).toLocaleString()}</span>
              </header>
              <p>Measured value: {alert.valeurMesuree}</p>
              <p>Threshold: {alert.seuilDeclenche}</p>
              <small>Capteur {alert.capteurId}</small>
            </article>
          )}
        />

        <Panel
          title="Maintenance orders"
          subtitle="Opened, started, closed or cancelled from factory"
          items={snapshot.maintenanceOrders}
          renderItem={(order) => (
            <article className="item-card warning" key={order.uuid}>
              <header>
                <strong>{order.reference}</strong>
                <span>{order.statut}</span>
              </header>
              <p>{order.description}</p>
              <small>{order.type} on machine {order.machineId}</small>
            </article>
          )}
        />

        <Panel
          title="Machine status"
          subtitle="Projected state used by the frontend"
          items={snapshot.machines}
          renderItem={(machine) => (
            <article className="item-card neutral" key={machine.uuid}>
              <header>
                <strong>{machine.nom}</strong>
                <span>{machine.statut}</span>
              </header>
              <p>{machine.type} · {machine.modele}</p>
              <small>Ligne {machine.ligneId ?? "n/a"}</small>
            </article>
          )}
        />
      </section>

      <section className="feed-panel">
        <div className="panel-heading">
          <h2>Live event feed</h2>
          <p>Every line below comes from an SSE push emitted after demoConsumer updates its projection.</p>
        </div>
        <div className="feed-list">
          {events.length === 0 ? <p className="empty-state">No live events yet.</p> : null}
          {events.map((event, index) => (
            <div className="feed-row" key={`${event.entityId}-${index}`}>
              <span className="feed-domain">{event.domain}</span>
              <strong>{event.eventType}</strong>
              <span className="feed-id">{event.entityId}</span>
            </div>
          ))}
        </div>
      </section>
    </main>
  );
}

function StatCard({ label, value, accent }) {
  return (
    <article className={`stat-card ${accent}`}>
      <span>{label}</span>
      <strong>{value}</strong>
    </article>
  );
}

function Panel({ title, subtitle, items, renderItem }) {
  return (
    <section className="panel">
      <div className="panel-heading">
        <h2>{title}</h2>
        <p>{subtitle}</p>
      </div>
      <div className="panel-body">
        {items.length === 0 ? <p className="empty-state">Waiting for projected data...</p> : null}
        {items.map(renderItem)}
      </div>
    </section>
  );
}
