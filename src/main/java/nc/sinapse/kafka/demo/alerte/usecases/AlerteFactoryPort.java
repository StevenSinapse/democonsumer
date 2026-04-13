package nc.sinapse.kafka.demo.alerte.usecases;

import nc.sinapse.kafka.demo.alerte.entities.Alerte;

import java.util.Optional;
import java.util.UUID;

/**
 * Port sortant HTTP — rappel API factory (thin event pattern).
 * <p>Permet de récupérer l'état complet d'une alerte à partir de son identifiant,
 * après réception d'un événement Kafka léger.</p>
 */
public interface AlerteFactoryPort {

    /**
     * Récupère une alerte complète depuis l'API factory.
     *
     * @param alerteId l'identifiant de l'alerte (entityId du message Kafka)
     * @return l'alerte si trouvée, vide sinon
     */
    Optional<Alerte> recupererAlerte(UUID alerteId);
}
