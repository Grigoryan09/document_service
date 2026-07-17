package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.dto.BuyerInfoDto;
import am.agro_trade.document_service.dto.ManagerInfoDto;
import am.agro_trade.document_service.dto.ProductDto;
import am.agro_trade.document_service.dto.SellerInfoDto;
import am.agro_trade.document_service.dto.document.OrderDocumentGenerateRequest;
import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.exception.DocumentProcessingException;
import am.agro_trade.document_service.exception.TemplateLoadException;
import am.agro_trade.document_service.service.DocumentGenerator;
import am.agro_trade.document_service.utils.OrderTemplateKeys;
import am.agro_trade.document_service.utils.TemplateProperties;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderDocumentGenerator implements DocumentGenerator<OrderDocumentGenerateRequest> {

    private final ResourceLoader resourceLoader;
    private final TemplateProperties templateProperties;

    @Override
    public DocumentType getType() {
        return DocumentType.ORDER;
    }

    @Override
    public String generate(OrderDocumentGenerateRequest dto) {

        InputStream is = loadTemplate();

        WordprocessingMLPackage word;
        try {
            word = WordprocessingMLPackage.load(is);
        } catch (Docx4JException e) {
            throw new DocumentProcessingException(
                    "Failed to process DOCX document for order: " + dto.orderId(), e);
        }
        MainDocumentPart mainPart = word.getMainDocumentPart();

        replaceVariables(mainPart, dto);

        return Base64.getEncoder().encodeToString(toByteArray(word));
    }

    private InputStream loadTemplate() {
        Resource resource = resourceLoader.getResource(templateProperties.getOrder());
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            throw new TemplateLoadException("Failed to load DOCX template", e);
        }
    }

    private void replaceVariables(MainDocumentPart mainPart, OrderDocumentGenerateRequest dto) {
        BuyerInfoDto buyer = dto.buyerInfoDto();
        SellerInfoDto seller = dto.sellerInfoDto();
        ManagerInfoDto manager = dto.managerInfoDto();
        ProductDto product = dto.productDto();

        Map<String, String> variables = new HashMap<>();
        variables.put(OrderTemplateKeys.ORDER_ID, String.valueOf(dto.orderId()));
        variables.put(OrderTemplateKeys.CREATED_AT, safe(dto.createdAt()));

        variables.put(OrderTemplateKeys.BUYER_FULL_NAME, safe(buyer.fullName()));
        variables.put(OrderTemplateKeys.BUYER_ADDRESS, safe(buyer.address()));
        variables.put(OrderTemplateKeys.BUYER_EMAIL, safe(buyer.email()));
        variables.put(OrderTemplateKeys.BUYER_PHONE_NUMBER, safe(buyer.phoneNumber()));

        variables.put(OrderTemplateKeys.SELLER_FULL_NAME, safe(seller.fullName()));
        variables.put(OrderTemplateKeys.SELLER_ADDRESS, safe(seller.address()));
        variables.put(OrderTemplateKeys.SELLER_EMAIL, safe(seller.email()));
        variables.put(OrderTemplateKeys.SELLER_PHONE_NUMBER, safe(seller.phoneNumber()));

        variables.put(OrderTemplateKeys.MANAGER_FULL_NAME, safe(manager.fullName()));
        variables.put(OrderTemplateKeys.MANAGER_ADDRESS, safe(manager.address()));
        variables.put(OrderTemplateKeys.MANAGER_EMAIL, safe(manager.email()));
        variables.put(OrderTemplateKeys.MANAGER_PHONE_NUMBER, safe(manager.phoneNumber()));

        variables.put(OrderTemplateKeys.PRODUCT_NAME, safe(product.productName()));
        variables.put(OrderTemplateKeys.PRODUCT_TYPE, safe(product.productType()));
        variables.put(OrderTemplateKeys.QUANTITY, String.valueOf(dto.quantity()));
        variables.put(OrderTemplateKeys.TOTAL_PRICE, safe(dto.totalPrice()));

        try {
            mainPart.variableReplace(variables);
        } catch (JAXBException | Docx4JException e) {
            throw new DocumentProcessingException(
                    "Failed to process DOCX document for order: " + dto.orderId(), e);
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private byte[] toByteArray(WordprocessingMLPackage word) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            word.save(baos);
        } catch (Docx4JException e) {
            throw new DocumentProcessingException("Failed to process DOCX document for order", e);
        }
        return baos.toByteArray();
    }
}
