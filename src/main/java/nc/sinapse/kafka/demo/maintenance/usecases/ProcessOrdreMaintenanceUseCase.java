package nc.sinapse.kafka.demo.maintenance.usecases;

import java.util.UUID;

public interface ProcessOrdreMaintenanceUseCase {
    void traiter(String topic, UUID eventId, UUID ordreId, Long version, String eventType);
}
