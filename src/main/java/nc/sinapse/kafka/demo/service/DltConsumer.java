package nc.sinapse.kafka.demo.service;

import nc.sinapse.kafka.demo.model.OrderEvent;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DltConsumer {
/*    @KafkaListener(topics = "orders-dlt")
    public void handleDlt(OrderEvent event) {
        System.out.println("Failed event moved to DLT: " + event.getOrderId());
    }

    @DltHandler
    public void handleDeadLetter(String event) {
        System.out.println("Failed event sent to DLT: " +  event);
    }*/
}
