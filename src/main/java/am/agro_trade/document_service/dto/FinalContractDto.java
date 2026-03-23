package am.agro_trade.document_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FinalContractDto(

        @Valid
        @NotNull(message = "Approved amount rate must be provided")
        BigDecimal approvedAmount,

        @Valid
        @NotNull(message = "Approved period must be provided")
        int approvedPeriod,

        @Valid
        @NotNull
        ProductDto productDto,

        LocalDateTime createdAt

) {
}
