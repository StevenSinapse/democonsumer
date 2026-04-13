package nc.sinapse.kafka.demo.machine.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardStreamPort;
import nc.sinapse.kafka.demo.dashboard.usecases.DashboardUpdate;
import nc.sinapse.kafka.demo.machine.entities.Machine;
import nc.sinapse.kafka.demo.shared.processing.ProcessedEventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MachineService implements ProcessMachineUseCase {

    private final MachineRepository machineRepository;
    private final MachineFactoryPort machineFactoryPort;
    private final ProcessedEventService processedEventService;
    private final DashboardStreamPort dashboardStreamPort;

    @Override
    public void traiter(String topic, UUID eventId, UUID machineId, Long version, String eventType) {
        if (processedEventService.dejaTraite(eventId)) {
            log.info("[IDEMPOTENCE] event {} deja traite, ignore", eventId);
            return;
        }

        Machine machine = machineFactoryPort.recupererMachine(machineId)
                .orElseThrow(() -> new MachineNotFoundException(machineId));

        Machine projection = machineRepository.sauvegarder(machine);
        processedEventService.marquerCommeTraite(eventId, topic, machineId, eventType);
        dashboardStreamPort.diffuserMiseAJour(new DashboardUpdate(
                "machines",
                eventType,
                projection.uuid().toString(),
                projection
        ));

        log.info("[MACHINE] Traitee - uuid={} nom={} statut={}",
                machine.uuid(), machine.nom(), machine.statut());
    }
}
