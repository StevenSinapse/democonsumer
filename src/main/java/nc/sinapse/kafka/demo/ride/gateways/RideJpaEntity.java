package nc.sinapse.kafka.demo.ride.gateways;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideJpaEntity {

    @Id
    private UUID rideId;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @Column(name = "from_location", nullable = false)
    private String fromLocation;

    @Column(name = "to_location", nullable = false)
    private String toLocation;

    @Column(nullable = false)
    private Instant departureTime;
}
