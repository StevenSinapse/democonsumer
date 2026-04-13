package nc.sinapse.kafka.demo.order.consumers;

/**
 * Message Kafka reçu depuis le topic {@code orders}.
 *
 * @param orderId  identifiant de la commande
 * @param product  nom du produit
 * @param quantity quantité commandée
 */
public record OrderKafkaMessage(
        String orderId,
        String product,
        int quantity
) {
}
