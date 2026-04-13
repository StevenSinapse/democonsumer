package nc.sinapse.kafka.demo.maintenance.entities;

import nc.sinapse.kafka.demo.maintenance.enums.StatutMaintenance;
import nc.sinapse.kafka.demo.maintenance.enums.TypeMaintenance;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrdreMaintenance(
        UUID uuid,
        Long version,
        String reference,
        TypeMaintenance type,
        StatutMaintenance statut,
        LocalDateTime dateOuverture,
        LocalDateTime dateDebut,
        LocalDateTime dateFin,
        String description,
        UUID machineId,
        UUID usineId,
        UUID employeId
) {
}
