package nc.sinapse.kafka.demo.ride.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.ride.entities.Ride;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RideService implements ProcessRideUseCase {

    private final RideRepository rideRepository;

    @Override
    public void traiter(Ride ride) {
        rideRepository.sauvegarderRide(ride);
        log.info("[RIDE] Traité — rideId={} de={} vers={}", ride.rideId(), ride.from(), ride.to());
    }
}
