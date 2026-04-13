package nc.sinapse.kafka.demo.order.gateways;

import lombok.RequiredArgsConstructor;
import nc.sinapse.kafka.demo.order.entities.Order;
import nc.sinapse.kafka.demo.order.usecases.OrderRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order sauvegarderOrder(Order order) {
        OrderJpaEntity entite = versEntiteJpa(order);
        return versDomaine(orderJpaRepository.save(entite));
    }

    private Order versDomaine(OrderJpaEntity jpa) {
        return new Order(jpa.getOrderId(), jpa.getProduct(), jpa.getQuantity());
    }

    private OrderJpaEntity versEntiteJpa(Order domaine) {
        return OrderJpaEntity.builder()
                .orderId(domaine.orderId())
                .product(domaine.product())
                .quantity(domaine.quantity())
                .build();
    }
}
