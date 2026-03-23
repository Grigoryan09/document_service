package am.agro_trade.document_service.endpoint.impl;

import am.agro_trade.document_service.dto.document.DocumentGenerateDto;
import am.agro_trade.document_service.dto.document.GeneratedContractResponse;
import am.agro_trade.document_service.endpoint.DocumentV1API;
import am.agro_trade.document_service.enums.DocumentFormat;
import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.service.impl.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class DocumentV1Endpoint implements DocumentV1API {

    private final DocumentService documentService;

    @Override
    @PostMapping("/generate-document")
    public GeneratedContractResponse generate(@RequestBody @Valid DocumentGenerateDto dto) {
        return new GeneratedContractResponse(documentService.generate(dto, DocumentType.valueOf(dto.documentType())), DocumentFormat.DOCX.name(), LocalDateTime.now());
    }
}
