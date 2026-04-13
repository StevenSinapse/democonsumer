package nc.sinapse.kafka.demo.maintenance.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.maintenance.usecases.ProcessOrdreMaintenanceUseCase;
import nc.sinapse.kafka.demo.shared.kafka.ThinEventMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrdreMaintenanceKafkaConsumer {

    private final ProcessOrdreMaintenanceUseCase processOrdreMaintenanceUseCase;
    private final AtomicInteger callCount = new AtomicInteger(0);

    @RetryableTopic(attempts = "4", dltTopicSuffix = "-dlt")
    @KafkaListener(topics = "maintenance.ordres", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consommer(ThinEventMessage event, Acknowledgment ack) {
        log.info("[MAINTENANCE] #{} eventId={} entityId={} version={} type={}",
                callCount.incrementAndGet(), event.eventId(), event.entityId(), event.version(), event.eventType());
        processOrdreMaintenanceUseCase.traiter(
                "maintenance.ordres", event.eventId(), event.entityId(), event.version(), event.eventType());
        ack.acknowledge();
    }
}
