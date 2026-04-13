package nc.sinapse.kafka.demo.alerte.usecases;

import java.util.UUID;

public class AlerteNotFoundException extends RuntimeException {

    public AlerteNotFoundException(UUID alerteId) {
        super("Alerte introuvable dans la factory : " + alerteId);
    }
}
