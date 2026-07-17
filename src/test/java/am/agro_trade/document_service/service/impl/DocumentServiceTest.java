package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.exception.DocumentGeneratorNotFoundException;
import am.agro_trade.document_service.service.DocumentGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentGeneratorFactory factory;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void generate_delegatesToGeneratorResolvedFromFactory() {
        DocumentGenerator<String> generator = mockGenerator();
        doReturn(generator).when(factory).getGenerator(DocumentType.CONTRACT);
        when(generator.generate("payload")).thenReturn("base64-result");

        String result = documentService.generate("payload", DocumentType.CONTRACT);

        assertThat(result).isEqualTo("base64-result");
        verify(factory).getGenerator(DocumentType.CONTRACT);
        verify(generator).generate("payload");
    }

    @Test
    void generate_propagatesExceptionWhenGeneratorMissing() {
        when(factory.getGenerator(any()))
                .thenThrow(new DocumentGeneratorNotFoundException(DocumentType.ORDER));

        assertThatThrownBy(() -> documentService.generate("payload", DocumentType.ORDER))
                .isInstanceOf(DocumentGeneratorNotFoundException.class);
    }

    @SuppressWarnings("unchecked")
    private DocumentGenerator<String> mockGenerator() {
        return org.mockito.Mockito.mock(DocumentGenerator.class);
    }
}
