package am.agro_trade.document_service.dto.document;


import java.time.LocalDateTime;

public record GeneratedDocumentResponse(

        String encodeDocument,
        String format,
        LocalDateTime createdAt
) {
}
