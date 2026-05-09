package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.dto.PaymentRowDto;
import am.agro_trade.document_service.dto.document.ContractDocumentGenerateRequest;
import am.agro_trade.document_service.enums.DocumentType;
import am.agro_trade.document_service.exception.DocumentProcessingException;
import am.agro_trade.document_service.exception.TemplateLoadException;
import am.agro_trade.document_service.service.DocumentGenerator;
import am.agro_trade.document_service.utils.TemplateKeys;
import am.agro_trade.document_service.utils.TemplateProperties;
import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContractDocumentGenerator implements DocumentGenerator {

    private final ResourceLoader resourceLoader;
    private final TemplateProperties templateProperties;

    @Override
    public DocumentType getType() {
        return DocumentType.CONTRACT;
    }

    @Override
    public String generate(ContractDocumentGenerateRequest dto) {

        InputStream is = loadTemplate();

        WordprocessingMLPackage word;
        try {
            word = WordprocessingMLPackage.load(is);
        } catch (Docx4JException e) {
            throw new DocumentProcessingException(
                    "Failed to process DOCX document for client: " + TemplateKeys.CLIENT_FULL_NAME, e);
        }
        MainDocumentPart mainPart = word.getMainDocumentPart();

        replaceVariables(mainPart, dto);
        fillPaymentTable(mainPart, dto.paymentRowDtoList());

        return Base64.getEncoder().encodeToString(toByteArray(word));

    }

    private InputStream loadTemplate() {
        Resource resource = resourceLoader.getResource(templateProperties.getContract());
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            throw new TemplateLoadException("Failed to load DOCX template", e);
        }
    }

    private void replaceVariables(MainDocumentPart mainPart, ContractDocumentGenerateRequest dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        var bank = dto.bankDto();
        var offer = dto.offerDto();
        var contract = dto.finalContractDto();
        var client = dto.clientInfoDto();
        var passport = client != null ? client.passportInfo() : null;

        Map<String, String> variables = new HashMap<>();

        variables.put(TemplateKeys.BANK_NAME, safe(bank.bankName()));
        variables.put(TemplateKeys.BANK_PHONE_NUMBER, safe(bank.phoneNumber()));
        variables.put(TemplateKeys.OFFER_TYPE, safe(offer.offerType()));
        variables.put(TemplateKeys.INTEREST_RATE, decimalFormat(offer.interestRate()));
        variables.put(TemplateKeys.APPROVED_AMOUNT, decimalFormat(contract.approvedAmount()));
        variables.put(TemplateKeys.APPROVED_PERIOD, String.valueOf(contract.approvedPeriod()));
        variables.put(TemplateKeys.DATE, contract.createdAt().format(formatter));

        if (client != null) {
            variables.put(TemplateKeys.CLIENT_FULL_NAME, safe(client.fullName()));
            variables.put(TemplateKeys.EMAIL, safe(client.email()));
            variables.put(TemplateKeys.PHONE_NUMBER, safe(client.phoneNumber()));

            if (passport != null) {
                variables.put(TemplateKeys.PASSPORT_NUMBER, safe(passport.passportNumber()));
            }
        }
        try {
            mainPart.variableReplace(variables);
        } catch (JAXBException | Docx4JException e) {
            throw new DocumentProcessingException(
                    "Failed to process DOCX document for client: " + TemplateKeys.CLIENT_FULL_NAME, e);
        }
    }

    private void fillPaymentTable(MainDocumentPart mainPart, List<PaymentRowDto> rows) {
        ClassFinder finder = new ClassFinder(Tbl.class);
        new TraversalUtil(mainPart.getContent(), finder);

        Tbl table = (Tbl) finder.results.getFirst();
        ObjectFactory factory = new ObjectFactory();

        for (PaymentRowDto row : rows) {
            Tr tr = factory.createTr();
            tr.getContent().add(createCell(String.valueOf(row.month())));
            tr.getContent().add(createCell(decimalFormat(row.monthlyPayment())));
            tr.getContent().add(createCell(decimalFormat(row.interest())));
            tr.getContent().add(createCell(decimalFormat(row.principal())));
            tr.getContent().add(createCell(decimalFormat(row.balance())));

            table.getContent().add(tr);
        }
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String decimalFormat(BigDecimal value) {
        if (value == null) return "";
        return new DecimalFormat("#.##").format(value);
    }

    private byte[] toByteArray(WordprocessingMLPackage word) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            word.save(baos);
        } catch (Docx4JException e) {
            throw new DocumentProcessingException(
                    "Failed to process DOCX document for client: " + TemplateKeys.CLIENT_FULL_NAME, e);
        }
        return baos.toByteArray();
    }

    private Tc createCell(String text) {
        ObjectFactory factory = new ObjectFactory();

        Tc tc = factory.createTc();
        P p = factory.createP();
        R r = factory.createR();
        Text t = factory.createText();

        t.setValue(text);
        r.getContent().add(t);
        p.getContent().add(r);
        tc.getContent().add(p);

        return tc;
    }
}

