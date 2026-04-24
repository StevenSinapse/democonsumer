package nc.sinapse.kafka.demo.shared.processing;

import java.time.Instant;
import java.util.UUID;

public record ThinEventMessage(
        UUID eventId,
        String eventType,
        UUID entityId,
        Long version,
        Instant timestamp
) {
}
