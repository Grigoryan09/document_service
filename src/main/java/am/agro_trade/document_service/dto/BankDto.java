package am.agro_trade.document_service.dto;

import jakarta.validation.constraints.NotBlank;


public record BankDto(

        @NotBlank(message = "Bank name cannot be empty")
        String bankName,

        @NotBlank(message = "Bank phone number cannot be empty")
        String phoneNumber,

        @NotBlank(message = "Bank license number cannot be empty")
        String licenseNumber
) {
}
