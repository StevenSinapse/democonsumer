package nc.sinapse.kafka.demo.maintenance.usecases;

import nc.sinapse.kafka.demo.maintenance.entities.OrdreMaintenance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdreMaintenanceRepository {
    OrdreMaintenance sauvegarder(OrdreMaintenance ordre);

    Optional<OrdreMaintenance> trouver(UUID uuid);

    List<OrdreMaintenance> listerRecents(int limite);
}
