package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.service.DocumentGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentGeneratorFactory factory;

    @SuppressWarnings("unchecked")
    public <T> String generate(T dto, DocumentType type) {
        return ((DocumentGenerator<T>) factory.getGenerator(type)).generate(dto);
    }
}
