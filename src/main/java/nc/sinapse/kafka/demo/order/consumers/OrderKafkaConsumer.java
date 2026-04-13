package nc.sinapse.kafka.demo.order.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.order.entities.Order;
import nc.sinapse.kafka.demo.order.usecases.ProcessOrderUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * Adaptateur entrant Kafka pour les commandes.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderKafkaConsumer {

    private final ProcessOrderUseCase processOrderUseCase;

    /*@KafkaListener(topics = "orders", groupId = "notification-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void consommer(OrderKafkaMessage message,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                          Acknowledgment ack) {
        log.info("[ORDER] orderId={} partition={}", message.orderId(), partition);
        Order order = new Order(message.orderId(), message.product(), message.quantity());
        processOrderUseCase.traiter(order);
        ack.acknowledge();
    }*/
}
