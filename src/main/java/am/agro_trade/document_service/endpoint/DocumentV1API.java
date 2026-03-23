package am.agro_trade.document_service.endpoint;

import am.agro_trade.document_service.dto.document.DocumentGenerateDto;
import am.agro_trade.document_service.dto.document.GeneratedContractResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API for document generation operations.
 * Provides endpoints to generate different types of documents.
 */
@RequestMapping("/document-service/api/v1")
public interface DocumentV1API {

    /**
     * Generates a document based on provided data.
     *
     * @param dto data required for document generation
     * @return generated document response
     */
    @PostMapping("/generate-document")
    GeneratedContractResponse generate(@RequestBody @Valid DocumentGenerateDto dto);

}
