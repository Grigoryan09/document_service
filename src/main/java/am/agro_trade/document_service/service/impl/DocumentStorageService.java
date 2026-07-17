package am.agro_trade.document_service.service.impl;

import am.agro_trade.document_service.exception.DocumentProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Persists generated documents (Base64-encoded DOCX) to the configured output directory.
 */
@Service
@Slf4j
public class DocumentStorageService {

    private static final DateTimeFormatter FILE_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    @Value("${document.output.path}")
    private String outputPath;

    /**
     * Decodes a Base64 DOCX payload and writes it to disk.
     *
     * @param base64Document Base64-encoded DOCX content
     * @param clientName     used as part of the file name
     * @return the path of the written file
     */
    public Path save(String base64Document, String clientName) {
        byte[] bytes = Base64.getDecoder().decode(base64Document);

        try {
            Path directory = Paths.get(outputPath);
            Files.createDirectories(directory);

            String fileName = buildFileName(clientName);
            Path target = directory.resolve(fileName);
            Files.write(target, bytes);

            log.info("Document saved to {}", target.toAbsolutePath());
            return target;
        } catch (IOException e) {
            throw new DocumentProcessingException(
                    "Failed to save generated document for client: " + clientName, e);
        }
    }

    private String buildFileName(String clientName) {
        String safeName = (clientName == null || clientName.isBlank())
                ? "document"
                : clientName.trim().replaceAll("[^\\p{L}\\p{N}]+", "_");
        return safeName + "_" + LocalDateTime.now().format(FILE_TIMESTAMP) + ".docx";
    }
}