package nc.sinapse.kafka.demo.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

   /* @KafkaListener(topics = "topicdetest", groupId = "demo-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }*/


}