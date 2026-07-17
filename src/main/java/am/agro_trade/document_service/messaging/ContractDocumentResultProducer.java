package am.agro_trade.document_service.messaging;

import am.agro_trade.document_service.dto.document.ContractDocumentResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContractDocumentResultProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${document.kafka.contract-result-topic}")
    private String contractResultTopic;

    public void send(ContractDocumentResultEvent event) {
        kafkaTemplate.send(contractResultTopic, String.valueOf(event.externalRequestId()), event);
    }
}