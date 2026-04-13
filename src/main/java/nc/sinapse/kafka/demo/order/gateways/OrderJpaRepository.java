package nc.sinapse.kafka.demo.order.gateways;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, String> {
}
