package nc.sinapse.kafka.demo.ride.gateways;

import lombok.RequiredArgsConstructor;
import nc.sinapse.kafka.demo.ride.entities.Ride;
import nc.sinapse.kafka.demo.ride.usecases.RideRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RideRepositoryAdapter implements RideRepository {

    private final RideJpaRepository rideJpaRepository;

    @Override
    public Ride sauvegarderRide(Ride ride) {
        RideJpaEntity entite = versEntiteJpa(ride);
        return versDomaine(rideJpaRepository.save(entite));
    }

    private Ride versDomaine(RideJpaEntity jpa) {
        return new Ride(jpa.getRideId(), jpa.getDriverId(),
                jpa.getFromLocation(), jpa.getToLocation(), jpa.getDepartureTime());
    }

    private RideJpaEntity versEntiteJpa(Ride domaine) {
        return RideJpaEntity.builder()
                .rideId(domaine.rideId())
                .driverId(domaine.driverId())
                .fromLocation(domaine.from())
                .toLocation(domaine.to())
                .departureTime(domaine.departureTime())
                .build();
    }
}
