package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.dto.BuyerInfoDto;
import am.agro_trade.document_service.dto.ManagerInfoDto;
import am.agro_trade.document_service.dto.ProductDto;
import am.agro_trade.document_service.dto.SellerInfoDto;
import am.agro_trade.document_service.dto.document.OrderDocumentGenerateRequest;
import am.agro_trade.document_service.enums.DocumentType;
import org.junit.jupiter.api.Test;

import static am.agro_trade.document_service.service.impl.DocxTestSupport.extractText;
import static am.agro_trade.document_service.service.impl.DocxTestSupport.resourceLoader;
import static am.agro_trade.document_service.service.impl.DocxTestSupport.templateProperties;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end test of {@link OrderDocumentGenerator}: real template + real docx4j processing.
 */
class OrderDocumentGeneratorIT {

    private final OrderDocumentGenerator generator =
            new OrderDocumentGenerator(resourceLoader(), templateProperties());

    private static OrderDocumentGenerateRequest sampleRequest() {
        return new OrderDocumentGenerateRequest(
                42L,
                "2026-07-14",
                new BuyerInfoDto("Buyer One", "1 Buyer St", "buyer@example.com", "+37400000001"),
                new SellerInfoDto("Seller Two", "2 Seller Ave", "seller@example.com", "+37400000002"),
                new ManagerInfoDto("Manager Three", "3 Manager Rd", "manager@example.com", "+37400000003"),
                new ProductDto("Wheat", "GRAIN"),
                1000L,
                "250000",
                7L,
                99L);
    }

    @Test
    void getType_isOrder() {
        assertThat(generator.getType()).isEqualTo(DocumentType.ORDER);
    }

    @Test
    void generate_replacesAllPlaceholdersWithDtoValues() {
        String base64 = generator.generate(sampleRequest());

        assertThat(base64).isNotBlank();
        String text = extractText(base64);

        assertThat(text).doesNotContain("${");
        assertThat(text)
                .contains("42")
                .contains("2026-07-14")
                .contains("Buyer One").contains("1 Buyer St").contains("buyer@example.com").contains("+37400000001")
                .contains("Seller Two").contains("2 Seller Ave").contains("seller@example.com").contains("+37400000002")
                .contains("Manager Three").contains("3 Manager Rd").contains("manager@example.com").contains("+37400000003")
                .contains("Wheat").contains("GRAIN")
                .contains("1000")
                .contains("250000");
    }

    @Test
    void generate_replacesNullOptionalFieldsWithEmptyString() {
        OrderDocumentGenerateRequest dto = new OrderDocumentGenerateRequest(
                7L, null,
                new BuyerInfoDto(null, null, null, null),
                new SellerInfoDto(null, null, null, null),
                new ManagerInfoDto(null, null, null, null),
                new ProductDto(null, null),
                0L, null, 0L, 0L);

        String text = extractText(generator.generate(dto));

        // null values are coerced to "" via safe(...) so no unresolved placeholders remain
        assertThat(text).doesNotContain("${");
        assertThat(text).contains("7");
    }
}
