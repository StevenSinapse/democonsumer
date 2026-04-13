package nc.sinapse.kafka.demo.machine.usecases;

import nc.sinapse.kafka.demo.machine.entities.Machine;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MachineRepository {
    Machine sauvegarder(Machine machine);

    Optional<Machine> trouver(UUID uuid);

    List<Machine> listerTous();
}
