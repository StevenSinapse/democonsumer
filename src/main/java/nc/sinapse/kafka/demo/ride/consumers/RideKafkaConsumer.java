package nc.sinapse.kafka.demo.ride.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.ride.entities.Ride;
import nc.sinapse.kafka.demo.ride.usecases.ProcessRideUseCase;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Adaptateur entrant Kafka pour les trajets publiés.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RideKafkaConsumer {

    private final ProcessRideUseCase processRideUseCase;
    private final AtomicInteger callCount = new AtomicInteger(0);

    /*@KafkaListener(topics = "ride-published", groupId = "consumer-order-group",
            containerFactory = "rideKafkaListenerContainerFactory")
    public void consommer(RideKafkaMessage message, Acknowledgment ack) {
        log.info("[RIDE] #{} rideId={}", callCount.incrementAndGet(), message.rideId());

        Ride ride = new Ride(message.rideId(), message.driverId(),
                message.from(), message.to(), message.departureTime());
        processRideUseCase.traiter(ride);
        ack.acknowledge();
    }*/
}
