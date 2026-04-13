package nc.sinapse.kafka.demo.maintenance.gateways;

import lombok.RequiredArgsConstructor;
import nc.sinapse.kafka.demo.maintenance.entities.OrdreMaintenance;
import nc.sinapse.kafka.demo.maintenance.usecases.OrdreMaintenanceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrdreMaintenanceRepositoryAdapter implements OrdreMaintenanceRepository {

    private final OrdreMaintenanceJpaRepository ordreMaintenanceJpaRepository;

    @Override
    public OrdreMaintenance sauvegarder(OrdreMaintenance ordre) {
        return versDomaine(ordreMaintenanceJpaRepository.save(versEntite(ordre)));
    }

    @Override
    public Optional<OrdreMaintenance> trouver(UUID uuid) {
        return ordreMaintenanceJpaRepository.findById(uuid).map(this::versDomaine);
    }

    @Override
    public List<OrdreMaintenance> listerRecents(int limite) {
        return ordreMaintenanceJpaRepository.findTop20ByOrderByDateOuvertureDesc().stream()
                .limit(limite)
                .map(this::versDomaine)
                .toList();
    }

    private OrdreMaintenance versDomaine(OrdreMaintenanceJpaEntity entite) {
        return new OrdreMaintenance(
                entite.getUuid(),
                entite.getSourceVersion(),
                entite.getReference(),
                entite.getType(),
                entite.getStatut(),
                entite.getDateOuverture(),
                entite.getDateDebut(),
                entite.getDateFin(),
                entite.getDescription(),
                entite.getMachineId(),
                entite.getUsineId(),
                entite.getEmployeId()
        );
    }

    private OrdreMaintenanceJpaEntity versEntite(OrdreMaintenance ordre) {
        return OrdreMaintenanceJpaEntity.builder()
                .uuid(ordre.uuid())
                .sourceVersion(ordre.version())
                .reference(ordre.reference())
                .type(ordre.type())
                .statut(ordre.statut())
                .dateOuverture(ordre.dateOuverture())
                .dateDebut(ordre.dateDebut())
                .dateFin(ordre.dateFin())
                .description(ordre.description())
                .machineId(ordre.machineId())
                .usineId(ordre.usineId())
                .employeId(ordre.employeId())
                .build();
    }
}
