package nc.sinapse.kafka.demo.alerte.gateways;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlerteJpaRepository extends JpaRepository<AlerteJpaEntity, UUID> {

    boolean existsByUuid(UUID uuid);

    List<AlerteJpaEntity> findByCapteurId(UUID capteurId);

    List<AlerteJpaEntity> findTop20ByOrderByHorodatageDesc();
}
