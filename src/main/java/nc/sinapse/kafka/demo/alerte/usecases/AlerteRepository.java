package nc.sinapse.kafka.demo.alerte.usecases;

import nc.sinapse.kafka.demo.alerte.entities.Alerte;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port sortant de persistance pour les alertes.
 */
public interface AlerteRepository {

    boolean existsByUuid(UUID uuid);

    Alerte sauvegarderAlerte(Alerte alerte);

    Optional<Alerte> trouverAlerte(UUID uuid);

    List<Alerte> listerParCapteur(UUID capteurId);

    List<Alerte> listerRecentes(int limite);
}
