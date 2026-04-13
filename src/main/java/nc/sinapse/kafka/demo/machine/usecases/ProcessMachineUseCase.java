package nc.sinapse.kafka.demo.machine.usecases;

import java.util.UUID;

public interface ProcessMachineUseCase {
    void traiter(String topic, UUID eventId, UUID machineId, Long version, String eventType);
}
