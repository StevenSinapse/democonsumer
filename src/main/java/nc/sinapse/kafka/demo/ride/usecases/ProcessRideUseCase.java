package nc.sinapse.kafka.demo.ride.usecases;

import nc.sinapse.kafka.demo.ride.entities.Ride;

/** Port entrant — traitement d'un trajet reçu depuis Kafka. */
public interface ProcessRideUseCase {
    void traiter(Ride ride);
}
