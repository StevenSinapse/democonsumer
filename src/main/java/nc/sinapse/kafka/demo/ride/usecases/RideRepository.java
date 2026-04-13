package nc.sinapse.kafka.demo.ride.usecases;

import nc.sinapse.kafka.demo.ride.entities.Ride;

/** Port sortant de persistance pour les trajets. */
public interface RideRepository {
    Ride sauvegarderRide(Ride ride);
}
