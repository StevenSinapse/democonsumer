package nc.sinapse.kafka.demo.shared.processing.gateways;

import lombok.RequiredArgsConstructor;
import nc.sinapse.kafka.demo.shared.processing.usecases.ProcessedEventRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProcessedEventRepositoryAdapter implements ProcessedEventRepository {

    private final ProcessedEventJpaRepository processedEventJpaRepository;

    @Override
    public boolean existsById(UUID eventId) {
        return processedEventJpaRepository.existsById(eventId);
    }

    @Override
    public void save(UUID eventId, String topic, UUID entityId, String eventType) {
        processedEventJpaRepository.save(ProcessedEventJpaEntity.builder()
                .eventId(eventId)
                .topic(topic)
                .entityId(entityId)
                .eventType(eventType)
                .processedAt(Instant.now())
                .build());
    }
}
