package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.utils.TemplateProperties;
import org.docx4j.TextUtils;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Base64;
import java.util.Locale;

/**
 * Shared helpers for the DOCX generator integration tests. These tests exercise the real
 * docx4j pipeline against the real templates on the classpath — no mocking of the generation core.
 */
final class DocxTestSupport {

    private DocxTestSupport() {
    }

    static ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }

    static TemplateProperties templateProperties() {
        TemplateProperties properties = new TemplateProperties();
        properties.setContract("classpath:/templates/contract.docx");
        properties.setOrder("classpath:/templates/order.docx");
        return properties;
    }

    /** Formats a decimal exactly as the generators do (fixed {@code Locale.ROOT}). */
    static String fmt(BigDecimal value) {
        return new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ROOT)).format(value);
    }

    /** Decodes a Base64-encoded DOCX and returns all of its body text as a single string. */
    static String extractText(String base64Docx) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64Docx);
            WordprocessingMLPackage word =
                    WordprocessingMLPackage.load(new ByteArrayInputStream(bytes));
            return TextUtils.getText(word.getMainDocumentPart().getJaxbElement());
        } catch (Exception e) {
            throw new IllegalStateException("Failed to re-parse generated DOCX", e);
        }
    }
}
