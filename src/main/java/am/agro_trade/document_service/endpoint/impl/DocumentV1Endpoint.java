package am.agro_trade.document_service.endpoint.impl;

import am.agro_trade.document_service.dto.document.ContractDocumentGenerateRequest;
import am.agro_trade.document_service.dto.document.GeneratedContractResponse;
import am.agro_trade.document_service.dto.document.OrderDocumentGenerateRequest;
import am.agro_trade.document_service.endpoint.DocumentV1API;
import am.agro_trade.document_service.enums.DocumentFormat;
import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.service.impl.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DocumentV1Endpoint implements DocumentV1API {

    private final DocumentService documentService;

    @Override
    public GeneratedContractResponse generateBankContract(@RequestBody @Valid ContractDocumentGenerateRequest dto) {
        return new GeneratedContractResponse(documentService.generate(dto,
                DocumentType.valueOf(dto.documentType())),
                DocumentFormat.DOCX.name(),
                dto.clientInfoDto().fullName(),
                LocalDateTime.now());
    }

    @Override
    public GeneratedContractResponse generateOrderDocument(OrderDocumentGenerateRequest request) {
        return null;
    }
}
