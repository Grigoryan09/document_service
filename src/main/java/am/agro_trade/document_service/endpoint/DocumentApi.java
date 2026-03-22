package am.agro_trade.document_service.endpoint;

import am.agro_trade.document_service.dto.document.DocumentGenerateDto;
import am.agro_trade.document_service.dto.document.GeneratedDocumentResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DocumentApi {

    @PostMapping("/generate-document")
    GeneratedDocumentResponse generate(@RequestBody @Valid DocumentGenerateDto dto);

}
