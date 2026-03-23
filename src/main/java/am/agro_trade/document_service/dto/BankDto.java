package am.agro_trade.document_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record BankDto(

        @NotNull(message = "Bank name cannot be empty")
        String bankName,

        @NotBlank(message = "Bank phone number cannot be empty")
        String phoneNumber
) {
}
