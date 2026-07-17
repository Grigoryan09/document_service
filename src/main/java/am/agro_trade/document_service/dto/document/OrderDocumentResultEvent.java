package am.agro_trade.document_service.dto.document;

public record OrderDocumentResultEvent(
        long chatId,
        long senderUserId,
        String fileName,
        String base64Document
) {
}