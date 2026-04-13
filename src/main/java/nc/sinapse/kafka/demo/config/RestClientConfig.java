package nc.sinapse.kafka.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configuration du client HTTP pour appeler l'API factory (thin event pattern).
 * <p>Quand un événement Kafka arrive, le consommateur rappelle l'API
 * factory via {@code GET /factory/alertes/{id}} pour récupérer l'état complet.</p>
 */
@Configuration
public class RestClientConfig {

    @Value("${factory.api.base-url}")
    private String factoryBaseUrl;

    @Bean
    public RestClient factoryRestClient() {
        return RestClient.builder()
                .baseUrl(factoryBaseUrl)
                .build();
    }
}
