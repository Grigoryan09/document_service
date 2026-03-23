package am.agro_trade.document_service.exception;

import am.agro_trade.document_service.enums.DocumentType;

public class DocumentGeneratorNotFoundException extends RuntimeException {
    public DocumentGeneratorNotFoundException(DocumentType type) {
        super("No document generator found for type: " + type);
    }
}
