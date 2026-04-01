package nc.sinapse.kafka.demo.service;

import nc.sinapse.kafka.demo.model.AlerteKafkaMessage;
import nc.sinapse.kafka.demo.model.OrderEvent;
import nc.sinapse.kafka.demo.model.RidePublishedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.concurrent.atomic.AtomicInteger;

@Service
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);
    private final AtomicInteger callCount = new AtomicInteger(0);

    /*@KafkaListener(topics = "orders", groupId = "notification-group")
    public void consume(OrderEvent order, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition){
        System.out.println("Sending email for order: " + order.getOrderId() + " with partition : " + partition);
    }

    @KafkaListener(topics = "orders")
    public void consumeTest(OrderEvent order, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition){
        System.out.println("testgroup: " + order.getOrderId()  + " with partition : " + partition);
    }*/

   /* @KafkaListener(topics = "ride-published")
    public void consumeRide(RidePublishedEvent ride){
        System.out.println("Ride sent : " + ride.getRideId());
    }*/

    @KafkaListener(topics = "ride-published-dlt", groupId = "dlt-monitor-group",
            containerFactory = "dltKafkaListenerContainerFactory")
    void onDeadLetter(ConsumerRecord<String, String> record,
                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                      @Header("kafka_dlt-exception-message") String exceptionMessage) {
        log.warn("[DLT] Dead letter received: key={} topic={} reason={} payload={}",
                record.key(), topic, exceptionMessage, record.value());
    }

    @KafkaListener(topics = "ride-published", groupId = "consumer-order-group",
            containerFactory = "kafkaListenerContainerFactory")
    void consume(ConsumerRecord<String, RidePublishedEvent> record) {
        log.info("[KAFKA NEW] consume() invocation #{} for key={} and value={}",
                callCount.incrementAndGet(), record.key(), record.value());
        //throw new RuntimeException("Simulated failure — forcing DLT");
    }

    @RetryableTopic(attempts = "4",
            dltTopicSuffix = "-dlt"
    )
    @KafkaListener(topics = "iot.alertes",
            containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    void consumeFactory(AlerteKafkaMessage event, Acknowledgment ack) {
        log.info("[FACTORY] increment = {}, entityId = {}, version = {}",
                callCount.incrementAndGet(), event.entityId, event.version);

        /*if (dossierRepository.existsByLastEventId(event.eventId())) {
            log.info("[IDEMPOTENCE] eventId={} déjà traité, ignoré", event.eventId());
            ack.acknowledge();
            return;
        }*/

        /*if (realEvent.getVersion() > event.getVersion()) {
            log.info("[IDEMPOTENCE] version obsolète, ignoré");
            ack.acknowledge();
            return;
        }*/

        /*try {
            dossier.setLastEventId(event.eventId());
            dossierRepository.save(dossier);
        } catch (DataIntegrityViolationException ex) {
            // doublon détecté
            ack.acknowledge();
            return;
        }*/

    }

}