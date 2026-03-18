package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.dto.PaymentRowDto;
import am.agro_trade.document_service.dto.document.DocumentGenerateDto;
import am.agro_trade.document_service.service.DocumentService;
import jakarta.xml.bind.JAXBException;
import org.docx4j.TraversalUtil;
import org.docx4j.finders.ClassFinder;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Value("${template.path.contract}")
    String TEMP_DIR;

    @Override
    public byte[] getDocumentContract(DocumentGenerateDto documentGenerateDto) {
        try {
            WordprocessingMLPackage word = WordprocessingMLPackage.load(new File(TEMP_DIR));
            MainDocumentPart mainPart = word.getMainDocumentPart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


            Map<String, String> variables = new HashMap<>();

            variables.put("bankName",documentGenerateDto.bankDto().bankName());
            variables.put("bankPhoneNumber",documentGenerateDto.bankDto().phoneNumber());
            variables.put("offerType",documentGenerateDto.offerDto().offerType());
            variables.put("interestRate",documentGenerateDto.offerDto().interestRate().toString());
            variables.put("approvedAmount",documentGenerateDto.finalContractDto().approvedAmount().toString());
            variables.put("approvedPeriod",String.valueOf(documentGenerateDto.finalContractDto().approvedPeriod()));
            variables.put("clientFullName",documentGenerateDto.clientInfoDto().fullName());
            variables.put("email",documentGenerateDto.clientInfoDto().email());
            variables.put("phoneNumber",documentGenerateDto.clientInfoDto().phoneNumber());
            variables.put("passportNumber",documentGenerateDto.clientInfoDto().passportInfo().passportNumber());
            variables.put("date",documentGenerateDto.finalContractDto().createdAt().format(formatter));

            word.getMainDocumentPart().variableReplace(variables);

            // Находим первую таблицу в документе
            ClassFinder finder = new ClassFinder(Tbl.class);
            new TraversalUtil(mainPart.getContent(), finder);
            Tbl table = (Tbl) finder.results.get(0);

            ObjectFactory factory = new ObjectFactory();

            // Пропускаем первую строку (заголовок)
            List<Object> tableRows = table.getContent();

            // Начинаем с первой строки (после заголовка)
            for (PaymentRowDto row : documentGenerateDto.paymentRowDtoList()) {
                Tr tr = factory.createTr();
                tr.getContent().add(createCell(String.valueOf(row.month())));
                tr.getContent().add(createCell(row.monthlyPayment().toString()));
                tr.getContent().add(createCell(row.interest().toString()));
                tr.getContent().add(createCell(row.principal().toString()));
                tr.getContent().add(createCell(row.balance().toString()));
                table.getContent().add(tr);
            }

            String dateTime = documentGenerateDto.finalContractDto().createdAt().format(formatter);
            String fileName = "contract_" + documentGenerateDto.bankDto().bankName() + "_" + documentGenerateDto.clientInfoDto().fullName() + "_" + dateTime + ".docx";

            File output = new File("generated/" + fileName);
            word.save(output);


        } catch (Docx4JException | JAXBException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Tc createCell(String value) {

        ObjectFactory factory = new ObjectFactory();

        Tc cell = factory.createTc();
        P paragraph = factory.createP();
        R run = factory.createR();
        Text text = factory.createText();

        text.setValue(value);
        text.setSpace("preserve"); // важно!

        run.getContent().add(text);
        paragraph.getContent().add(run);
        cell.getContent().add(paragraph);

        return cell;
    }
}
