package nc.sinapse.kafka.demo.machine.usecases;

import java.util.UUID;

public class MachineNotFoundException extends RuntimeException {
    public MachineNotFoundException(UUID machineId) {
        super("Machine introuvable: " + machineId);
    }
}
