package am.agro_trade.document_service.dto;

import jakarta.validation.constraints.NotBlank;

public record PassportInfoDto(

        @NotBlank(message = "Passport number cannot be empty")
        String passportNumber

) {
}
