package am.agro_trade.document_service.config;

import am.agro_trade.document_service.dto.document.ContractDocumentGenerateRequest;
import am.agro_trade.document_service.dto.document.OrderDocumentGenerateRequest;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka consumer configuration for document generation requests.
 * Mirrors the producer in the agro_trade core service, which serializes with
 * {@code JacksonJsonSerializer} and disables type headers
 * ({@code spring.json.add.type.headers=false}). Because the type is not sent in
 * the headers, the target type is provided explicitly here.
 */
@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Bean
    public ConsumerFactory<String, ContractDocumentGenerateRequest> contractConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        config.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, ContractDocumentGenerateRequest.class.getName());
        config.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "am.agro_trade.document_service.*");
        config.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ContractDocumentGenerateRequest> contractKafkaListenerContainerFactory(
            ConsumerFactory<String, ContractDocumentGenerateRequest> contractConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, ContractDocumentGenerateRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(contractConsumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderDocumentGenerateRequest> orderConsumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class);
        config.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, OrderDocumentGenerateRequest.class.getName());
        config.put(JacksonJsonDeserializer.TRUSTED_PACKAGES, "am.agro_trade.document_service.*");
        config.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderDocumentGenerateRequest> orderKafkaListenerContainerFactory(
            ConsumerFactory<String, OrderDocumentGenerateRequest> orderConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, OrderDocumentGenerateRequest> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderConsumerFactory);
        return factory;
    }
}