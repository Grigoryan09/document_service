package am.agro_trade.document_service.dto;

import java.math.BigDecimal;

public record PaymentRowDto(

        int month,
        BigDecimal monthlyPayment,
        BigDecimal interest,
        BigDecimal principal,
        BigDecimal balance
) {
}
