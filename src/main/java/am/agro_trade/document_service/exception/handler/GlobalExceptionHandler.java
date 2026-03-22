package am.agro_trade.document_service.exception.handler;

import am.agro_trade.document_service.dto.document.ErrorResponse;
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
    public ResponseEntity<ErrorResponse> handleDocumentGeneration(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<ErrorResponse.FieldError> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorResponse.FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();

        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(Instant.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError("Document Generation Failed");
        response.setMessage("Validation Failed");
        response.setPath(request.getRequestURI());
        response.setDetails(details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
