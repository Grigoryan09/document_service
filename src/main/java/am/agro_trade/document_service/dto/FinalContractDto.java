package am.agro_trade.document_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FinalContractDto(

        BigDecimal approvedAmount,
        int approvedPeriod,
        ProductDto productDto,
        LocalDateTime createdAt

) {
}
