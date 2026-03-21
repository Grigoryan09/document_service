package am.agro_trade.document_service.endpoint;

import am.agro_trade.document_service.dto.document.DocumentGenerateDto;
import am.agro_trade.document_service.dto.document.GeneratedDocumentResponse;
import am.agro_trade.document_service.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class DocumentEndpoint {

    private final DocumentService documentService;

    @PostMapping("/generation")
    public GeneratedDocumentResponse generateDocument(@RequestBody DocumentGenerateDto documentGenerateDto) {
        return new GeneratedDocumentResponse(documentService.generateContractDocument(documentGenerateDto),"docx", LocalDateTime.now());
    }
}
