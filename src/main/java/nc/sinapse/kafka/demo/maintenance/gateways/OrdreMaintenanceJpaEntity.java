package nc.sinapse.kafka.demo.maintenance.gateways;

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
import nc.sinapse.kafka.demo.maintenance.enums.StatutMaintenance;
import nc.sinapse.kafka.demo.maintenance.enums.TypeMaintenance;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(schema = "demo_consumer", name = "ordres_maintenance_projection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdreMaintenanceJpaEntity {

    @Id
    private UUID uuid;

    @Column(name = "source_version")
    private Long sourceVersion;

    @Column(nullable = false)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeMaintenance type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutMaintenance statut;

    @Column(name = "date_ouverture", nullable = false)
    private LocalDateTime dateOuverture;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(name = "machine_id", nullable = false)
    private UUID machineId;

    @Column(name = "usine_id", nullable = false)
    private UUID usineId;

    @Column(name = "employe_id")
    private UUID employeId;
}
