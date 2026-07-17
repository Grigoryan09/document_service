package am.agro_trade.document_service.messaging;

import am.agro_trade.document_service.dto.document.OrderDocumentGenerateRequest;
import am.agro_trade.document_service.dto.document.OrderDocumentResultEvent;
import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.service.impl.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderDocumentConsumer {

    private final DocumentService documentService;
    private final OrderDocumentResultProducer orderDocumentResultProducer;

    @KafkaListener(
            topics = "${document.kafka.order-generate-topic}",
            containerFactory = "orderKafkaListenerContainerFactory")
    public void onOrderDocumentRequest(OrderDocumentGenerateRequest request) {
        log.info("Received order document generation request for order {}", request.orderId());

        try {
            String base64Document = documentService.generate(request, DocumentType.ORDER);
            String fileName = "order-%d.docx".formatted(request.orderId());

            orderDocumentResultProducer.send(new OrderDocumentResultEvent(
                    request.chatId(),
                    request.senderUserId(),
                    fileName,
                    base64Document
            ));
            log.info("Generated order document for order {} and published result", request.orderId());
        } catch (Exception e) {
            log.error("Failed to generate order document for order {}: {}",
                    request.orderId(), e.getMessage(), e);
        }
    }
}