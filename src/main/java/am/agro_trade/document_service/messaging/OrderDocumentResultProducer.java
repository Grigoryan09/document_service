package am.agro_trade.document_service.messaging;

import am.agro_trade.document_service.dto.document.OrderDocumentResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDocumentResultProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${document.kafka.order-result-topic}")
    private String orderResultTopic;

    public void send(OrderDocumentResultEvent event) {
        kafkaTemplate.send(orderResultTopic, String.valueOf(event.chatId()), event);
    }
}