package am.agro_trade.document_service.endpoint;

import am.agro_trade.document_service.dto.document.ContractDocumentGenerateRequest;
import am.agro_trade.document_service.dto.document.GeneratedContractResponse;
import am.agro_trade.document_service.dto.document.OrderDocumentGenerateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API for document generation operations.
 * Provides endpoints to generate different types of documents.
 */
@RestController
@RequestMapping("/document-service/api/v1/generate-document")
public interface DocumentV1API {

    /**
     * Generates a document based on provided data.
     *
     * @param request data required for document generation
     * @return generated document response
     */
    @PostMapping("/contract")
    GeneratedContractResponse generateBankContract(@RequestBody @Valid ContractDocumentGenerateRequest request);

    @PostMapping("/order")
    GeneratedContractResponse generateOrderDocument(@RequestBody @Valid OrderDocumentGenerateRequest request);

}
