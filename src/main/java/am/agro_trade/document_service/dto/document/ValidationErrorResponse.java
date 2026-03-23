package am.agro_trade.document_service.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorResponse {

    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<FieldError> details;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError {

        private String field;
        private String message;

    }
}
