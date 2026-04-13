package nc.sinapse.kafka.demo.maintenance.gateways.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.maintenance.entities.OrdreMaintenance;
import nc.sinapse.kafka.demo.maintenance.enums.StatutMaintenance;
import nc.sinapse.kafka.demo.maintenance.enums.TypeMaintenance;
import nc.sinapse.kafka.demo.maintenance.usecases.OrdreMaintenanceFactoryPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrdreMaintenanceFactoryAdapter implements OrdreMaintenanceFactoryPort {

    private final RestClient factoryRestClient;

    @Override
    public Optional<OrdreMaintenance> recupererOrdre(UUID ordreId) {
        try {
            ReponseOrdreMaintenance response = factoryRestClient.get()
                    .uri("/factory/maintenances/{id}", ordreId)
                    .retrieve()
                    .body(ReponseOrdreMaintenance.class);
            return Optional.ofNullable(response).map(this::versDomaine);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("[HTTP] Ordre de maintenance {} introuvable dans factory", ordreId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("[HTTP] Echec de recuperation de l'ordre {} : {}", ordreId, e.getMessage());
            throw new RuntimeException("Impossible de recuperer l'ordre " + ordreId, e);
        }
    }

    private OrdreMaintenance versDomaine(ReponseOrdreMaintenance r) {
        return new OrdreMaintenance(
                r.uuid(),
                r.version(),
                r.reference(),
                r.type(),
                r.statut(),
                r.dateOuverture(),
                r.dateDebut(),
                r.dateFin(),
                r.description(),
                r.machineId(),
                r.usineId(),
                r.employeId()
        );
    }

    private record ReponseOrdreMaintenance(
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
}
