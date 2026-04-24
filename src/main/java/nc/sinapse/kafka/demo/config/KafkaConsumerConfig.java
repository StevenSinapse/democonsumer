package nc.sinapse.kafka.demo.config;

import jakarta.annotation.PostConstruct;
import nc.sinapse.kafka.demo.ride.consumers.RideKafkaMessage;
import nc.sinapse.kafka.demo.shared.processing.ThinEventMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, ThinEventMessage> consumerFactory() {
        Map<String, Object> config = baseConsumerConfig();
        JacksonJsonDeserializer<ThinEventMessage> valueDeserializer =
                new JacksonJsonDeserializer<>(ThinEventMessage.class, false);
        valueDeserializer.addTrustedPackages("*");
        valueDeserializer.ignoreTypeHeaders();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new ErrorHandlingDeserializer<>(new StringDeserializer()),
                new ErrorHandlingDeserializer<>(valueDeserializer)
        );
    }

    @Bean
    public ConsumerFactory<String, RideKafkaMessage> rideConsumerFactory() {
        Map<String, Object> config = baseConsumerConfig();
        JacksonJsonDeserializer<RideKafkaMessage> valueDeserializer =
                new JacksonJsonDeserializer<>(RideKafkaMessage.class, false);
        valueDeserializer.addTrustedPackages("*");
        valueDeserializer.ignoreTypeHeaders();
        return new DefaultKafkaConsumerFactory<>(
                config,
                new ErrorHandlingDeserializer<>(new StringDeserializer()),
                new ErrorHandlingDeserializer<>(valueDeserializer)
        );
    }

    @Bean
    public DefaultErrorHandler errorHandler(
            @Qualifier("dltKafkaTemplate") KafkaTemplate<String, Object> dltKafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(dltKafkaTemplate,
                (record, ex) -> {
                    if (record.topic().endsWith("-dlt")) {
                        log.warn("[DLT] Skipping re-routing for already dead-lettered record: topic={}",
                                record.topic());
                        return null;
                    }
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    log.warn("[DLT] Sending to DLT: key={} reason={}", record.key(), cause.getMessage());
                    return new TopicPartition(record.topic() + "-dlt", record.partition());
                });
        recoverer.setThrowIfNoDestinationReturned(false);
        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, new FixedBackOff(1_000L, 3L));
        handler.setRetryListeners((record, ex, deliveryAttempt) ->
                log.warn("[RETRY] key={} attempt={}/3 reason={}", record.key(), deliveryAttempt,
                        ex != null ? ex.getMessage() : "unknown"));
        return handler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ThinEventMessage> kafkaListenerContainerFactory(
            DefaultErrorHandler errorHandler,
            ConsumerFactory<String, ThinEventMessage> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, ThinEventMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RideKafkaMessage> rideKafkaListenerContainerFactory(
            DefaultErrorHandler errorHandler,
            ConsumerFactory<String, RideKafkaMessage> rideConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, RideKafkaMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(rideConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @PostConstruct
    public void logConfig() {
        log.info("[CONFIG] Kafka consumer bootstrap-servers={}", bootstrapServers);
    }

    private Map<String, Object> baseConsumerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return config;
    }
}
