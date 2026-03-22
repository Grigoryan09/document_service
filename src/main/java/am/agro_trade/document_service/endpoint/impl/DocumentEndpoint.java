package am.agro_trade.document_service.endpoint.impl;

import am.agro_trade.document_service.dto.document.DocumentGenerateDto;
import am.agro_trade.document_service.dto.document.GeneratedDocumentResponse;
import am.agro_trade.document_service.endpoint.DocumentApi;
import am.agro_trade.document_service.enums.DocumentFormat;
import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.service.impl.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/document-service/api/v1")
@RequiredArgsConstructor
public class DocumentEndpoint implements DocumentApi {

    private final DocumentService documentService;

    @Override
    @PostMapping("/generate-document")
    public GeneratedDocumentResponse generate(@RequestBody @Valid DocumentGenerateDto dto) {
        return new GeneratedDocumentResponse(documentService.generate(dto, DocumentType.valueOf(dto.documentType())), DocumentFormat.DOCX.name(), LocalDateTime.now());
    }
}
