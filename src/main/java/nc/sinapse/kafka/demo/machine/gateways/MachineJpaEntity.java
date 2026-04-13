package nc.sinapse.kafka.demo.machine.gateways;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nc.sinapse.kafka.demo.machine.enums.StatutMachine;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(schema = "demo_consumer", name = "machines_projection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MachineJpaEntity {

    @Id
    private UUID uuid;

    @Column(name = "source_version")
    private Long sourceVersion;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false)
    private String type;

    @Column(name = "date_acquisition")
    private LocalDate dateAcquisition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutMachine statut;

    @Column(name = "ligne_id")
    private UUID ligneId;
}
