package nc.sinapse.kafka.demo.alerte.gateways;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import nc.sinapse.kafka.demo.alerte.enums.TypeDepassement;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité JPA pour le stockage local des alertes IoT reçues depuis la factory.
 * <p>L'uuid est celui de la factory (pas auto-généré) — sert de clé d'idempotence.</p>
 */
@Entity
@Table(schema = "demo_consumer", name = "alertes_projection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlerteJpaEntity {

    /** UUID de la factory — clé naturelle et d'idempotence. */
    @Id
    private UUID uuid;

    @Column(name = "source_version")
    private Long sourceVersion;

    @Column(nullable = false)
    private LocalDateTime horodatage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDepassement typeDepassement;

    @Column(nullable = false)
    private Double valeurMesuree;

    @Column(nullable = false)
    private Double seuilDeclenche;

    @Column(name = "capteur_id", nullable = false)
    private UUID capteurId;

    @Column(name = "mesure_id", nullable = false)
    private UUID mesureId;
}
