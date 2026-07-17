package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.exception.DocumentGeneratorNotFoundException;
import am.agro_trade.document_service.service.DocumentGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DocumentGeneratorFactoryTest {

    private DocumentGenerator<?> generatorFor(DocumentType type) {
        DocumentGenerator<?> generator = mock(DocumentGenerator.class);
        when(generator.getType()).thenReturn(type);
        return generator;
    }

    @Test
    void getGenerator_returnsGeneratorMatchingRequestedType() {
        DocumentGenerator<?> contract = generatorFor(DocumentType.CONTRACT);
        DocumentGenerator<?> order = generatorFor(DocumentType.ORDER);
        DocumentGeneratorFactory factory = new DocumentGeneratorFactory(List.of(contract, order));

        assertThat(factory.getGenerator(DocumentType.CONTRACT)).isSameAs(contract);
        assertThat(factory.getGenerator(DocumentType.ORDER)).isSameAs(order);
    }

    @Test
    void getGenerator_throwsWhenNoGeneratorRegisteredForType() {
        DocumentGeneratorFactory factory =
                new DocumentGeneratorFactory(List.of(generatorFor(DocumentType.CONTRACT)));

        assertThatThrownBy(() -> factory.getGenerator(DocumentType.ORDER))
                .isInstanceOf(DocumentGeneratorNotFoundException.class)
                .hasMessageContaining(DocumentType.ORDER.name());
    }

    @Test
    void getGenerator_throwsWhenNoGeneratorsAtAll() {
        DocumentGeneratorFactory factory = new DocumentGeneratorFactory(List.of());

        assertThatThrownBy(() -> factory.getGenerator(DocumentType.CONTRACT))
                .isInstanceOf(DocumentGeneratorNotFoundException.class);
    }
}
