package nc.sinapse.kafka.demo.service;

import nc.sinapse.kafka.demo.model.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumerService {

    /*@KafkaListener(topics = "orders", groupId = "inventory-group")
    public void consume(OrderEvent order, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition){
        System.out.println("Inventory reserving product: " + order.getProduct() + " with partition : " + partition);
        if(order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }*/
}
