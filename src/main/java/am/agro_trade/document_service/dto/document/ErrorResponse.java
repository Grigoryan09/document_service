package am.agro_trade.document_service.dto.document;

import java.time.Instant;

public record ErrorResponse(

        Instant timestamp,
        String code,
        String message
) {
}
