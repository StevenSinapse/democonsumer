package nc.sinapse.kafka.demo.maintenance.usecases;

import java.util.UUID;

public class OrdreMaintenanceNotFoundException extends RuntimeException {
    public OrdreMaintenanceNotFoundException(UUID ordreId) {
        super("Ordre de maintenance introuvable: " + ordreId);
    }
}
