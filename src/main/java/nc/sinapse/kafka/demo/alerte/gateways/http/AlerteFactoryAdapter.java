package nc.sinapse.kafka.demo.alerte.gateways.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.alerte.entities.Alerte;
import nc.sinapse.kafka.demo.alerte.enums.TypeDepassement;
import nc.sinapse.kafka.demo.alerte.usecases.AlerteFactoryPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur sortant HTTP — implémente le thin event pattern.
 * <p>Appelle {@code GET /factory/alertes/{alerteId}} pour récupérer
 * l'état complet de l'alerte après réception d'un événement Kafka léger.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AlerteFactoryAdapter implements AlerteFactoryPort {

    private final RestClient factoryRestClient;

    @Override
    public Optional<Alerte> recupererAlerte(UUID alerteId) {
        try {
            ReponseAlerte response = factoryRestClient.get()
                    .uri("/factory/alertes/{id}", alerteId)
                    .retrieve()
                    .body(ReponseAlerte.class);
            return Optional.ofNullable(response).map(this::versDomaine);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("[HTTP] Alerte {} introuvable dans la factory", alerteId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("[HTTP] Échec de récupération de l'alerte {} : {}", alerteId, e.getMessage());
            throw new RuntimeException("Impossible de récupérer l'alerte " + alerteId, e);
        }
    }

    private Alerte versDomaine(ReponseAlerte r) {
        return new Alerte(r.uuid(), null, r.horodatage(), r.typeDepassement(),
                r.valeurMesuree(), r.seuilDeclenche(), r.capteurId(), r.mesureId());
    }

    /**
     * DTO miroir de {@code AlerteController.ReponseAlerte} dans le projet factory.
     * Endpoint : {@code GET /factory/alertes/{alerteId}}
     */
    private record ReponseAlerte(
            UUID uuid,
            LocalDateTime horodatage,
            TypeDepassement typeDepassement,
            Double valeurMesuree,
            Double seuilDeclenche,
            UUID capteurId,
            UUID mesureId
    ) {}
}
