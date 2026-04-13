package nc.sinapse.kafka.demo.shared.processing;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(schema = "demo_consumer", name = "processed_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedEventJpaEntity {

    @Id
    @Column(name = "event_id", nullable = false, updatable = false)
    private UUID eventId;

    @Column(nullable = false, length = 120)
    private String topic;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    @Column(name = "event_type", nullable = false, length = 120)
    private String eventType;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;
}
