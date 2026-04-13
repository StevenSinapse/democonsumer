package nc.sinapse.kafka.demo.ride.entities;

import java.time.Instant;
import java.util.UUID;

/**
 * @param rideId        identifiant unique du trajet
 * @param driverId      identifiant du conducteur
 * @param from          lieu de départ
 * @param to            lieu d'arrivée
 * @param departureTime heure de départ prévue
 */
public record Ride(
        UUID rideId,
        UUID driverId,
        String from,
        String to,
        Instant departureTime
) {
}
