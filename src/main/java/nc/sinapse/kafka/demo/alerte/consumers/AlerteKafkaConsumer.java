package nc.sinapse.kafka.demo.alerte.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.alerte.usecases.ProcessAlerteUseCase;
import nc.sinapse.kafka.demo.shared.kafka.ThinEventMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Adaptateur entrant Kafka pour les alertes IoT.
 * <p>
 * Reçoit un thin event depuis le topic {@code iot.alertes}, délègue le traitement
 * au port entrant {@link ProcessAlerteUseCase} qui se charge du rappel API factory
 * et de la persistance locale.
 * </p>
 * <p>Retry automatique : 4 tentatives, messages en erreur routés vers {@code iot.alertes-dlt}.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AlerteKafkaConsumer {

    private final ProcessAlerteUseCase processAlerteUseCase;
    private final AtomicInteger callCount = new AtomicInteger(0);

    @RetryableTopic(attempts = "4", dltTopicSuffix = "-dlt")
    @KafkaListener(topics = "iot.alertes", containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consommer(ThinEventMessage event, Acknowledgment ack) {
        log.info("[ALERTE] #{} eventId={} entityId={} version={}",
                callCount.incrementAndGet(), event.eventId(), event.entityId(), event.version());
        processAlerteUseCase.traiter("iot.alertes", event.eventId(), event.entityId(), event.version(), event.eventType());
        ack.acknowledge();
    }
}
