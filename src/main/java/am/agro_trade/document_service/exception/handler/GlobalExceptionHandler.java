package am.agro_trade.document_service.exception.handler;

import am.agro_trade.document_service.dto.document.ErrorResponse;
import am.agro_trade.document_service.dto.document.ValidationErrorResponse;
import am.agro_trade.document_service.exception.DocumentGeneratorNotFoundException;
import am.agro_trade.document_service.exception.DocumentProcessingException;
import am.agro_trade.document_service.exception.TemplateLoadException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleDocumentGeneration(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ValidationErrorResponse.FieldError> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationErrorResponse.FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ValidationErrorResponse response = new ValidationErrorResponse();
        response.setTimestamp(Instant.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Document Generation Failed");
        response.setMessage("Validation Failed");
        response.setPath(request.getRequestURI());
        response.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DocumentGeneratorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDocumentGeneratorNotFound(
            DocumentGeneratorNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(Instant.now(),"Document Generator Not Found",ex.getMessage()));
    }

    @ExceptionHandler(TemplateLoadException.class)
    public ResponseEntity<ErrorResponse> handleTemplateLoad(TemplateLoadException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(Instant.now(),"Template load error",ex.getMessage()));
    }

    @ExceptionHandler(DocumentProcessingException.class)
    public ResponseEntity<ErrorResponse> handleDocumentProcessing(DocumentProcessingException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(Instant.now(),"Template load error",ex.getMessage()));
    }
}
