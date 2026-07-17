package am.agro_trade.document_service.messaging;

import am.agro_trade.document_service.dto.document.ContractDocumentGenerateRequest;
import am.agro_trade.document_service.dto.document.ContractDocumentResultEvent;
import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.service.impl.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DocumentGenerationConsumer {

    private final DocumentService documentService;
    private final ContractDocumentResultProducer contractDocumentResultProducer;

    @KafkaListener(
            topics = "${document.kafka.contract-topic}",
            containerFactory = "contractKafkaListenerContainerFactory")
    public void onContractDocumentRequest(ContractDocumentGenerateRequest request) {
        String clientName = request.clientInfoDto() != null ? request.clientInfoDto().fullName() : "unknown";
        log.info("Received contract document generation request for client: {}", clientName);

        String base64Document = documentService.generate(
                request, DocumentType.valueOf(request.documentType()));

        String fileName = "contract-%d.docx".formatted(request.finalContractId());

        contractDocumentResultProducer.send(new ContractDocumentResultEvent(
                request.externalRequestId(),
                request.finalContractId(),
                fileName,
                base64Document
        ));

        log.info("Sent generated contract document result for external request {}", request.externalRequestId());
    }
}