package nc.sinapse.kafka.demo.maintenance.gateways;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrdreMaintenanceJpaRepository extends JpaRepository<OrdreMaintenanceJpaEntity, UUID> {
    List<OrdreMaintenanceJpaEntity> findTop20ByOrderByDateOuvertureDesc();
}
