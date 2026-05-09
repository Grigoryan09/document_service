package am.agro_trade.document_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OfferDto(

        @Valid
        BankDto bank,

        @Valid
        @NotBlank(message = "Offer type must be provided")
        String offerType,

        @Valid
        @NotNull(message = "Interest rate must be provided")
        BigDecimal interestRate,

        int maxDurationMonths,

        BigDecimal minAmount
) {
}
