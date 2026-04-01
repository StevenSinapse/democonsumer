package nc.sinapse.kafka.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class RidePublishedEvent {
    private UUID rideId;
    private UUID driverId;
    private String from;
    private String to;
    private Instant departureTime;
}
