package nc.sinapse.kafka.demo.ride.consumers;

import java.time.Instant;
import java.util.UUID;

/**
 * Message Kafka reçu depuis le topic {@code ride-published}.
 *
 * @param rideId        identifiant unique du trajet
 * @param driverId      identifiant du conducteur
 * @param from          lieu de départ
 * @param to            lieu d'arrivée
 * @param departureTime heure de départ prévue
 */
public record RideKafkaMessage(
        UUID rideId,
        UUID driverId,
        String from,
        String to,
        Instant departureTime
) {
}
