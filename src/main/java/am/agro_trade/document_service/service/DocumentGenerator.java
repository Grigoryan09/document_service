package am.agro_trade.document_service.service;

import am.agro_trade.document_service.dto.document.ContractDocumentGenerateRequest;
import am.agro_trade.document_service.enums.DocumentType;

public interface DocumentGenerator {

    DocumentType getType();

    String generate(ContractDocumentGenerateRequest dto);


}
