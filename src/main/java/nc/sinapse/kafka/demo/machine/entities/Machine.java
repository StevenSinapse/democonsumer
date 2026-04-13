package nc.sinapse.kafka.demo.machine.entities;

import nc.sinapse.kafka.demo.machine.enums.StatutMachine;

import java.time.LocalDate;
import java.util.UUID;

public record Machine(
        UUID uuid,
        Long version,
        String nom,
        String modele,
        String type,
        LocalDate dateAcquisition,
        StatutMachine statut,
        UUID ligneId
) {
}
