package nc.sinapse.kafka.demo.order.gateways;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderJpaEntity {

    @Id
    private String orderId;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private int quantity;
}
