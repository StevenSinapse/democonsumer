package nc.sinapse.kafka.demo.dashboard.usecases;

import java.util.List;
import java.util.Map;

public record DashboardSnapshot(
        Map<String, Integer> counts,
        List<Object> alerts,
        List<Object> maintenanceOrders,
        List<Object> machines
) {
}
