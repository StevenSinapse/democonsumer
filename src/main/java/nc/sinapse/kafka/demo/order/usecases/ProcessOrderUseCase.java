package nc.sinapse.kafka.demo.order.usecases;

import nc.sinapse.kafka.demo.order.entities.Order;

/** Port entrant — traitement d'une commande reçue depuis Kafka. */
public interface ProcessOrderUseCase {
    void traiter(Order order);
}
