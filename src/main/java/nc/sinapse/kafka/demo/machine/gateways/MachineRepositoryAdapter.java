package nc.sinapse.kafka.demo.machine.gateways;

import lombok.RequiredArgsConstructor;
import nc.sinapse.kafka.demo.machine.entities.Machine;
import nc.sinapse.kafka.demo.machine.usecases.MachineRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MachineRepositoryAdapter implements MachineRepository {

    private final MachineJpaRepository machineJpaRepository;

    @Override
    public Machine sauvegarder(Machine machine) {
        return versDomaine(machineJpaRepository.save(versEntite(machine)));
    }

    @Override
    public Optional<Machine> trouver(UUID uuid) {
        return machineJpaRepository.findById(uuid).map(this::versDomaine);
    }

    @Override
    public List<Machine> listerTous() {
        return machineJpaRepository.findAllByOrderByNomAsc().stream()
                .map(this::versDomaine)
                .toList();
    }

    private Machine versDomaine(MachineJpaEntity entite) {
        return new Machine(
                entite.getUuid(),
                entite.getSourceVersion(),
                entite.getNom(),
                entite.getModele(),
                entite.getType(),
                entite.getDateAcquisition(),
                entite.getStatut(),
                entite.getLigneId()
        );
    }

    private MachineJpaEntity versEntite(Machine machine) {
        return MachineJpaEntity.builder()
                .uuid(machine.uuid())
                .sourceVersion(machine.version())
                .nom(machine.nom())
                .modele(machine.modele())
                .type(machine.type())
                .dateAcquisition(machine.dateAcquisition())
                .statut(machine.statut())
                .ligneId(machine.ligneId())
                .build();
    }
}
