package am.agro_trade.document_service.dto.document;

public record ContractDocumentResultEvent(

        long externalRequestId,
        long finalContractId,
        String fileName,
        String base64Document

) {
}