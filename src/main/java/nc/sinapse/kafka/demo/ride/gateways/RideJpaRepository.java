package nc.sinapse.kafka.demo.ride.gateways;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RideJpaRepository extends JpaRepository<RideJpaEntity, UUID> {
}
