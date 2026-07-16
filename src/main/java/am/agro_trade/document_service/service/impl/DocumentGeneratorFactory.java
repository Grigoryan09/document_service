package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.exception.DocumentGeneratorNotFoundException;
import am.agro_trade.document_service.service.DocumentGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DocumentGeneratorFactory {

    private final Map<DocumentType, DocumentGenerator<?>> generators;

    public DocumentGeneratorFactory(List<DocumentGenerator<?>> generatorList) {
        this.generators = generatorList.stream()
                .collect(Collectors.toMap(DocumentGenerator::getType, Function.identity()));
    }

    public DocumentGenerator<?> getGenerator(DocumentType type) {
        DocumentGenerator<?> generator = generators.get(type);

        if (generator == null) {
            throw new DocumentGeneratorNotFoundException(type);
        }

        return generator;
    }
}
