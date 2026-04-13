package nc.sinapse.kafka.demo.alerte.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Moniteur DLT pour les alertes IoT.
 * <p>Consomme {@code iot.alertes-dlt} et trace les messages en erreur définitive.</p>
 */
@Component
@Slf4j
public class AlerteDltConsumer {

    @KafkaListener(topics = "iot.alertes-dlt", groupId = "dlt-monitor-group",
            containerFactory = "dltKafkaListenerContainerFactory")
    public void surveiller(ConsumerRecord<String, String> record,
                           @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                           @Header(value = "kafka_dlt-exception-message", required = false) String exceptionMessage) {
        log.error("[DLT] Message en erreur définitive — topic={} key={} reason={} payload={}",
                topic, record.key(), exceptionMessage, record.value());
    }
}
