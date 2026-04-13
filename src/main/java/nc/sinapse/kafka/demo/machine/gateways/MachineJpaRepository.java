package nc.sinapse.kafka.demo.machine.gateways;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MachineJpaRepository extends JpaRepository<MachineJpaEntity, UUID> {
    List<MachineJpaEntity> findAllByOrderByNomAsc();
}
