package nc.sinapse.kafka.demo.order.entities;

/**
 * @param orderId  identifiant unique de la commande
 * @param product  nom du produit commandé
 * @param quantity quantité commandée (doit être > 0)
 */
public record Order(
        String orderId,
        String product,
        int quantity
) {
}
