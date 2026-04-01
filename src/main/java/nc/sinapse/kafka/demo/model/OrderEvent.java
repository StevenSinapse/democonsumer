package nc.sinapse.kafka.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderEvent {
    private String orderId;
    private String product;
    private int quantity;
}
