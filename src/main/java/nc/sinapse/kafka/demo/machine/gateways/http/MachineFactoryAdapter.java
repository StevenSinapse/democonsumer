package nc.sinapse.kafka.demo.machine.gateways.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nc.sinapse.kafka.demo.machine.entities.Machine;
import nc.sinapse.kafka.demo.machine.enums.StatutMachine;
import nc.sinapse.kafka.demo.machine.usecases.MachineFactoryPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class MachineFactoryAdapter implements MachineFactoryPort {

    private final RestClient factoryRestClient;

    @Override
    public Optional<Machine> recupererMachine(UUID machineId) {
        try {
            ReponseMachine response = factoryRestClient.get()
                    .uri("/factory/machines/{id}", machineId)
                    .retrieve()
                    .body(ReponseMachine.class);
            return Optional.ofNullable(response).map(this::versDomaine);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("[HTTP] Machine {} introuvable dans factory", machineId);
            return Optional.empty();
        } catch (Exception e) {
            log.error("[HTTP] Echec de recuperation de la machine {} : {}", machineId, e.getMessage());
            throw new RuntimeException("Impossible de recuperer la machine " + machineId, e);
        }
    }

    private Machine versDomaine(ReponseMachine r) {
        return new Machine(
                r.uuid(),
                r.version(),
                r.nom(),
                r.modele(),
                r.type(),
                r.dateAcquisition(),
                r.statut(),
                r.ligneId()
        );
    }

    private record ReponseMachine(
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
}
