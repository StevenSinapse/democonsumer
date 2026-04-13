package nc.sinapse.kafka.demo.alerte.usecases;

import java.util.UUID;

/**
 * Port entrant — traitement d'un événement Kafka d'alerte IoT.
 * <p>Le consommateur Kafka appelle ce port après avoir reçu un thin event,
 * qui déclenchera le rappel API factory pour récupérer les données complètes.</p>
 */
public interface ProcessAlerteUseCase {

    /**
     * Traite un événement d'alerte reçu depuis Kafka.
     *
     * @param eventId  identifiant unique de l'événement Kafka (déduplication)
     * @param alerteId identifiant de l'alerte dans le système factory (entityId)
     * @param version  version de l'alerte au moment de l'émission
     */
    void traiter(String topic, UUID eventId, UUID alerteId, Long version, String eventType);
}
