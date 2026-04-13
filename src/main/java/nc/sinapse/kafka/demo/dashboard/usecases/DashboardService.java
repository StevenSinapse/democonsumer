package nc.sinapse.kafka.demo.dashboard.usecases;

import lombok.RequiredArgsConstructor;
import nc.sinapse.kafka.demo.alerte.usecases.AlerteRepository;
import nc.sinapse.kafka.demo.machine.usecases.MachineRepository;
import nc.sinapse.kafka.demo.maintenance.usecases.OrdreMaintenanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService implements DashboardQueryUseCase {

    private final AlerteRepository alerteRepository;
    private final OrdreMaintenanceRepository ordreMaintenanceRepository;
    private final MachineRepository machineRepository;
    private final DashboardStreamPort dashboardStreamPort;

    @Override
    public DashboardSnapshot chargerSnapshot() {
        List<Object> alerts = alerteRepository.listerRecentes(12).stream().map(Object.class::cast).toList();
        List<Object> maintenanceOrders = ordreMaintenanceRepository.listerRecents(12).stream().map(Object.class::cast).toList();
        List<Object> machines = machineRepository.listerTous().stream().map(Object.class::cast).toList();
        return new DashboardSnapshot(
                Map.of(
                        "alerts", alerts.size(),
                        "maintenanceOrders", maintenanceOrders.size(),
                        "machines", machines.size()
                ),
                alerts,
                maintenanceOrders,
                machines
        );
    }

    @Override
    public SseEmitter ouvrirFlux() {
        return dashboardStreamPort.ouvrirFlux();
    }
}
