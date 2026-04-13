package nc.sinapse.kafka.demo.maintenance.usecases;

import nc.sinapse.kafka.demo.maintenance.entities.OrdreMaintenance;

import java.util.Optional;
import java.util.UUID;

public interface OrdreMaintenanceFactoryPort {
    Optional<OrdreMaintenance> recupererOrdre(UUID ordreId);
}
