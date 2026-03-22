package am.agro_trade.document_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClientInfoDto(

        @NotBlank(message = "Full name cannot be empty")
        String fullName,

        @Email(message = "Must be a valid email")
        String email,

        @NotBlank(message = "Phone number cannot be empty")
        String phoneNumber,

        PassportInfoDto passportInfo
        
) {
}
