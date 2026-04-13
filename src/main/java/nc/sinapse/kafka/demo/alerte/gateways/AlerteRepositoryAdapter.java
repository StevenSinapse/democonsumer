package nc.sinapse.kafka.demo.alerte.gateways;

import lombok.RequiredArgsConstructor;
import nc.sinapse.kafka.demo.alerte.entities.Alerte;
import nc.sinapse.kafka.demo.alerte.usecases.AlerteRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur de persistance pour les alertes.
 * <p>Fait le pont entre le port du domaine et l'infrastructure JPA.</p>
 */
@Component
@RequiredArgsConstructor
public class AlerteRepositoryAdapter implements AlerteRepository {

    private final AlerteJpaRepository alerteJpaRepository;

    @Override
    public boolean existsByUuid(UUID uuid) {
        return alerteJpaRepository.existsByUuid(uuid);
    }

    @Override
    public Alerte sauvegarderAlerte(Alerte alerte) {
        AlerteJpaEntity entite = versEntiteJpa(alerte);
        return versDomaine(alerteJpaRepository.save(entite));
    }

    @Override
    public Optional<Alerte> trouverAlerte(UUID uuid) {
        return alerteJpaRepository.findById(uuid).map(this::versDomaine);
    }

    @Override
    public List<Alerte> listerParCapteur(UUID capteurId) {
        return alerteJpaRepository.findByCapteurId(capteurId).stream()
                .map(this::versDomaine)
                .toList();
    }

    @Override
    public List<Alerte> listerRecentes(int limite) {
        return alerteJpaRepository.findTop20ByOrderByHorodatageDesc().stream()
                .limit(limite)
                .map(this::versDomaine)
                .toList();
    }

    private Alerte versDomaine(AlerteJpaEntity jpa) {
        return new Alerte(
                jpa.getUuid(),
                jpa.getSourceVersion(),
                jpa.getHorodatage(),
                jpa.getTypeDepassement(),
                jpa.getValeurMesuree(),
                jpa.getSeuilDeclenche(),
                jpa.getCapteurId(),
                jpa.getMesureId()
        );
    }

    private AlerteJpaEntity versEntiteJpa(Alerte domaine) {
        return AlerteJpaEntity.builder()
                .uuid(domaine.uuid())
                .sourceVersion(domaine.version())
                .horodatage(domaine.horodatage())
                .typeDepassement(domaine.typeDepassement())
                .valeurMesuree(domaine.valeurMesuree())
                .seuilDeclenche(domaine.seuilDeclenche())
                .capteurId(domaine.capteurId())
                .mesureId(domaine.mesureId())
                .build();
    }
}
