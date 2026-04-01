package nc.sinapse.kafka.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class AlerteKafkaMessage {
    public UUID eventId;
    public String eventType;
    public UUID entityId;
    public Long version;
    public Instant timestamp;
}