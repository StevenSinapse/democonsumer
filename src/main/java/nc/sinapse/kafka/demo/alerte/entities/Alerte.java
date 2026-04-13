package nc.sinapse.kafka.demo.alerte.entities;

import nc.sinapse.kafka.demo.alerte.enums.TypeDepassement;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Représente une alerte IoT déclenchée par un dépassement de seuil.
 * Miroir de l'entité {@code Alerte} du projet factory (consommateur du même domaine).
 *
 * @param uuid            identifiant unique de l'alerte (= entityId du message Kafka)
 * @param version         version JPA au moment de l'émission
 * @param horodatage      date et heure du déclenchement
 * @param typeDepassement type de dépassement (MIN ou MAX)
 * @param valeurMesuree   valeur ayant déclenché l'alerte
 * @param seuilDeclenche  valeur du seuil dépassé
 * @param capteurId       identifiant du capteur (référence inter-BC)
 * @param mesureId        identifiant de la mesure associée
 */
public record Alerte(
        UUID uuid,
        Long version,
        LocalDateTime horodatage,
        TypeDepassement typeDepassement,
        Double valeurMesuree,
        Double seuilDeclenche,
        UUID capteurId,
        UUID mesureId
) {
}
