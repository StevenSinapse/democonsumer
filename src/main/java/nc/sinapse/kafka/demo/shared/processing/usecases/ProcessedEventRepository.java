package nc.sinapse.kafka.demo.shared.processing.usecases;

import java.util.UUID;

public interface ProcessedEventRepository {

    boolean existsById(UUID eventId);

    void save(UUID eventId, String topic, UUID entityId, String eventType);
}
