package nc.sinapse.kafka.demo.machine.usecases;

import nc.sinapse.kafka.demo.machine.entities.Machine;

import java.util.Optional;
import java.util.UUID;

public interface MachineFactoryPort {
    Optional<Machine> recupererMachine(UUID machineId);
}
