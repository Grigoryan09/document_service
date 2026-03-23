package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.dto.document.DocumentGenerateDto;
import am.agro_trade.document_service.enums.DocumentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentGeneratorFactory factory;

    public String generate(DocumentGenerateDto dto, DocumentType type) {
        return factory.getGenerator(type).generate(dto);
    }
}
