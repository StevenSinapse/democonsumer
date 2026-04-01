package nc.sinapse.kafka.demo.config;

import jakarta.annotation.PostConstruct;
import nc.sinapse.kafka.demo.model.RidePublishedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.AbstractKafkaListenerContainerFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Autowired
    private ApplicationContext context;

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Bean
    public ConsumerFactory<String, RidePublishedEvent> consumerFactory() {
        JacksonJsonDeserializer<RidePublishedEvent> deserializer =
                new JacksonJsonDeserializer<>(RidePublishedEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);
        ErrorHandlingDeserializer<RidePublishedEvent> errorHandlingDeserializer =
                new ErrorHandlingDeserializer<>(deserializer);
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092,localhost:9093,localhost:9094");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class);
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new DefaultKafkaConsumerFactory<>(
                config,
                new ErrorHandlingDeserializer<>(new StringDeserializer()),
                errorHandlingDeserializer
        );
    }

    @Bean
    public DefaultErrorHandler errorHandler(@Qualifier("dltKafkaTemplate") KafkaTemplate<String, byte[]> dltKafkaTemplate){
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(dltKafkaTemplate,
                (record, ex) -> {
                    if (record.topic().endsWith("-dlt")) {
                        log.warn("[DLT] Skipping re-routing for already dead-lettered record. topic={}",
                                record.topic());
                        return null;
                    }
                    Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                    log.warn("[DLT] Sending failed record to DLT. key={} reason={}",
                            record.key(), cause.getMessage());
                    return new TopicPartition(record.topic() + "-dlt", record.partition());
                });
        recoverer.setThrowIfNoDestinationReturned(false);
        FixedBackOff backoff = new FixedBackOff(1_000L, 3L);
        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backoff);
        handler.setRetryListeners((record, ex, deliveryAttempt) ->
                log.warn("[RETRY] key={} attempt={}/3 reason={}",
                        record.key(), deliveryAttempt, ex.getMessage())
        );
        return handler;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RidePublishedEvent>
    kafkaListenerContainerFactory(DefaultErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, RidePublishedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @PostConstruct
    public void checkBeans() {
        // list all factory beans
        String[] factories = context.getBeanNamesForType(ConcurrentKafkaListenerContainerFactory.class);
        log.info("[CONFIG] Found {} factories: {}", factories.length, Arrays.toString(factories));

        // list all error handler beans
        String[] errorHandlers = context.getBeanNamesForType(CommonErrorHandler.class);
        log.info("[CONFIG] Found {} error handlers: {}", errorHandlers.length, Arrays.toString(errorHandlers));
    }

}
