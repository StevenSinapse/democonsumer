package nc.sinapse.kafka.demo.machine.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.machine.usecases.ProcessMachineUseCase;
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
public class MachineKafkaConsumer {

    private final ProcessMachineUseCase processMachineUseCase;
    private final AtomicInteger callCount = new AtomicInteger(0);

    @RetryableTopic(attempts = "4", dltTopicSuffix = "-dlt")
    @KafkaListener(topics = "structure.machines", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consommer(ThinEventMessage event, Acknowledgment ack) {
        log.info("[MACHINE] #{} eventId={} entityId={} version={} type={}",
                callCount.incrementAndGet(), event.eventId(), event.entityId(), event.version(), event.eventType());
        processMachineUseCase.traiter(
                "structure.machines", event.eventId(), event.entityId(), event.version(), event.eventType());
        ack.acknowledge();
    }
}
