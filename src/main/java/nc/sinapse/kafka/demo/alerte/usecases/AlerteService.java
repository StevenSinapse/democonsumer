package nc.sinapse.kafka.demo.alerte.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.alerte.entities.Alerte;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardStreamPort;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardUpdate;
import nc.sinapse.kafka.demo.shared.processing.usecases.ProcessedEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AlerteService implements ProcessAlerteUseCase {

    private final AlerteRepository alerteRepository;
    private final AlerteFactoryPort alerteFactoryPort;
    private final ProcessedEventService processedEventService;
    private final DashboardStreamPort dashboardStreamPort;

    @Override
    public void traiter(String topic, UUID eventId, UUID alerteId, Long version, String eventType) {
        if (processedEventService.dejaTraite(eventId)) {
            log.info("[IDEMPOTENCE] event {} deja traite, ignore", eventId);
            return;
        }

        Alerte alerte = alerteFactoryPort.recupererAlerte(alerteId)
                .orElseThrow(() -> new AlerteNotFoundException(alerteId));

        Alerte projection = alerteRepository.sauvegarderAlerte(alerte);
        processedEventService.marquerCommeTraite(eventId, topic, alerteId, eventType);
        dashboardStreamPort.diffuserMiseAJour(new DashboardUpdate(
                "alerts",
                eventType,
                projection.uuid().toString(),
                projection
        ));

        log.info("[ALERTE] Traitee - uuid={} capteur={} type={} valeur={}",
                alerte.uuid(), alerte.capteurId(), alerte.typeDepassement(), alerte.valeurMesuree());
    }
}
