package am.agro_trade.document_service.service;

import am.agro_trade.document_service.enums.DocumentType;

public interface DocumentGenerator<T> {

    DocumentType getType();

    String generate(T dto);


}