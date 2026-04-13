package nc.sinapse.kafka.demo.order.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.order.entities.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService implements ProcessOrderUseCase {

    private final OrderRepository orderRepository;

    @Override
    public void traiter(Order order) {
        if (order.quantity() <= 0) {
            throw new IllegalArgumentException("La quantité doit être supérieure à 0 — orderId=" + order.orderId());
        }
        orderRepository.sauvegarderOrder(order);
        log.info("[ORDER] Traitée — orderId={} produit={} quantité={}", order.orderId(), order.product(), order.quantity());
    }
}
