package nc.sinapse.kafka.demo.order.usecases;

import nc.sinapse.kafka.demo.order.entities.Order;

/** Port sortant de persistance pour les commandes. */
public interface OrderRepository {
    Order sauvegarderOrder(Order order);
}
