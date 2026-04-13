package nc.sinapse.kafka.demo.shared.processing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessedEventService {

    private final ProcessedEventJpaRepository processedEventJpaRepository;

    public boolean dejaTraite(UUID eventId) {
        return processedEventJpaRepository.existsById(eventId);
    }

    public void marquerCommeTraite(UUID eventId, String topic, UUID entityId, String eventType) {
        processedEventJpaRepository.save(ProcessedEventJpaEntity.builder()
                .eventId(eventId)
                .topic(topic)
                .entityId(entityId)
                .eventType(eventType)
                .processedAt(Instant.now())
                .build());
    }
}
