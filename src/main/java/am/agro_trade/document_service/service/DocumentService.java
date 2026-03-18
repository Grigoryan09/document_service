package am.agro_trade.document_service.service;

import am.agro_trade.document_service.dto.document.DocumentGenerateDto;

public interface DocumentService {

    byte[] getDocumentContract(DocumentGenerateDto documentGenerateDto);
}
