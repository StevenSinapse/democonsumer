package nc.sinapse.kafka.demo.maintenance.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardStreamPort;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardUpdate;
import nc.sinapse.kafka.demo.maintenance.entities.OrdreMaintenance;
import nc.sinapse.kafka.demo.shared.processing.ProcessedEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrdreMaintenanceService implements ProcessOrdreMaintenanceUseCase {

    private final OrdreMaintenanceRepository ordreMaintenanceRepository;
    private final OrdreMaintenanceFactoryPort ordreMaintenanceFactoryPort;
    private final ProcessedEventService processedEventService;
    private final DashboardStreamPort dashboardStreamPort;

    @Override
    public void traiter(String topic, UUID eventId, UUID ordreId, Long version, String eventType) {
        if (processedEventService.dejaTraite(eventId)) {
            log.info("[IDEMPOTENCE] event {} deja traite, ignore", eventId);
            return;
        }

        OrdreMaintenance ordre = ordreMaintenanceFactoryPort.recupererOrdre(ordreId)
                .orElseThrow(() -> new OrdreMaintenanceNotFoundException(ordreId));

        OrdreMaintenance projection = ordreMaintenanceRepository.sauvegarder(ordre);
        processedEventService.marquerCommeTraite(eventId, topic, ordreId, eventType);
        dashboardStreamPort.diffuserMiseAJour(new DashboardUpdate(
                "maintenanceOrders",
                eventType,
                projection.uuid().toString(),
                projection
        ));

        log.info("[MAINTENANCE] Traitee - uuid={} reference={} statut={}",
                ordre.uuid(), ordre.reference(), ordre.statut());
    }
}
