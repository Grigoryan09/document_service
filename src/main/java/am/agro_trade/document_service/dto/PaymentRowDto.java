package am.agro_trade.document_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRowDto(

        @Valid
        @NotNull(message = "Month must be provided")
        int month,

        @Valid
        @NotNull(message = "Monthly payment must be provided")
        BigDecimal monthlyPayment,

        @Valid
        @NotNull(message = "Interest must be provided")
        BigDecimal interest,

        @Valid
        @NotNull(message = "Principal must be provided")
        BigDecimal principal,

        @Valid
        @NotNull(message = "Balance must be provided")
        BigDecimal balance
) {
}
