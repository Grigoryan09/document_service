package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.dto.BankDto;
import am.agro_trade.document_service.dto.ClientInfoDto;
import am.agro_trade.document_service.dto.FinalContractDto;
import am.agro_trade.document_service.dto.OfferDto;
import am.agro_trade.document_service.dto.PassportInfoDto;
import am.agro_trade.document_service.dto.PaymentRowDto;
import am.agro_trade.document_service.dto.ProductDto;
import am.agro_trade.document_service.dto.document.ContractDocumentGenerateRequest;
import am.agro_trade.document_service.enums.DocumentType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static am.agro_trade.document_service.service.impl.DocxTestSupport.extractText;
import static am.agro_trade.document_service.service.impl.DocxTestSupport.fmt;
import static am.agro_trade.document_service.service.impl.DocxTestSupport.resourceLoader;
import static am.agro_trade.document_service.service.impl.DocxTestSupport.templateProperties;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end test of {@link ContractDocumentGenerator}: real template + real docx4j processing.
 */
class ContractDocumentGeneratorIT {

    private final ContractDocumentGenerator generator =
            new ContractDocumentGenerator(resourceLoader(), templateProperties());

    private static ContractDocumentGenerateRequest sampleRequest(List<PaymentRowDto> rows) {
        ProductDto product = new ProductDto("Tractor", "MACHINERY");
        return new ContractDocumentGenerateRequest(
                new BankDto("Agro Bank", "+37411223344", "LIC-987"),
                new OfferDto(null, "STANDARD", new BigDecimal("12.5"), 24, new BigDecimal("1000")),
                new FinalContractDto(
                        new BigDecimal("500000"), 24, product,
                        LocalDateTime.of(2026, 7, 14, 10, 0)),
                new ClientInfoDto(
                        "John Doe", "john@example.com", "+37499112233",
                        new PassportInfoDto("AN1234567")),
                rows,
                DocumentType.CONTRACT.name());
    }

    @Test
    void getType_isContract() {
        assertThat(generator.getType()).isEqualTo(DocumentType.CONTRACT);
    }

    @Test
    void generate_replacesScalarPlaceholdersWithDtoValues() {
        String base64 = generator.generate(sampleRequest(List.of()));

        assertThat(base64).isNotBlank();
        String text = extractText(base64);

        // placeholders are gone
        assertThat(text).doesNotContain("${");
        // scalar values are merged in
        assertThat(text)
                .contains("Agro Bank")
                .contains("+37411223344")
                .contains("STANDARD")
                .contains(fmt(new BigDecimal("12.5")))
                .contains(fmt(new BigDecimal("500000")))
                .contains("24")
                .contains("2026-07-14")
                .contains("John Doe")
                .contains("john@example.com")
                .contains("AN1234567");
    }

    @Test
    void generate_appendsPaymentRowsIntoTable() {
        List<PaymentRowDto> rows = List.of(
                new PaymentRowDto(1, new BigDecimal("100.5"), new BigDecimal("10.25"),
                        new BigDecimal("90.25"), new BigDecimal("409.75")),
                new PaymentRowDto(2, new BigDecimal("100.5"), new BigDecimal("8.15"),
                        new BigDecimal("92.35"), new BigDecimal("317.4")));

        String text = extractText(generator.generate(sampleRequest(rows)));

        // decimalFormat("#.##") output for each row value (locale-aware, see fmt)
        assertThat(text)
                .contains(fmt(new BigDecimal("100.5")))
                .contains(fmt(new BigDecimal("10.25")))
                .contains(fmt(new BigDecimal("90.25")))
                .contains(fmt(new BigDecimal("409.75")))
                .contains(fmt(new BigDecimal("317.4")));
    }

    @Test
    void generate_handlesEmptyPaymentRows() {
        String base64 = generator.generate(sampleRequest(List.of()));
        assertThat(base64).isNotBlank();
    }
}
