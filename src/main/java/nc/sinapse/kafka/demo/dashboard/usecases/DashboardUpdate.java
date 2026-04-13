package nc.sinapse.kafka.demo.dashboard.usecases;

/**
 * Message SSE unifie vers le frontend.
 *
 * @param domain domaine source de la projection
 * @param eventType type d'evenement metier
 * @param entityId identifiant de l'entite projetee
 * @param payload contenu a afficher
 */
public record DashboardUpdate(
        String domain,
        String eventType,
        String entityId,
        Object payload
) {
}
