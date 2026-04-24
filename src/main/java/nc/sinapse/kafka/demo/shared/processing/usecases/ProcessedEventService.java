package nc.sinapse.kafka.demo.shared.processing.usecases;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessedEventService {

    private final ProcessedEventRepository processedEventRepository;

    public boolean dejaTraite(UUID eventId) {
        return processedEventRepository.existsById(eventId);
    }

    public void marquerCommeTraite(UUID eventId, String topic, UUID entityId, String eventType) {
        processedEventRepository.save(eventId, topic, entityId, eventType);
    }
}
