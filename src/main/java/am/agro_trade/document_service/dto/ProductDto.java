package am.agro_trade.document_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ProductDto(

        @Valid
        @NotBlank(message = "Product name cannot be empty")
        String productName,

        @Valid
        @NotBlank(message = "Product type must be provided")
        String productType
) {
}
