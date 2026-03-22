package am.agro_trade.document_service.dto.document;

import am.agro_trade.document_service.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DocumentGenerateDto(

        @Valid
        @NotNull
        BankDto bankDto,

        @Valid
        @NotNull
        OfferDto offerDto,

        @Valid
        @NotNull
        FinalContractDto finalContractDto,

        @Valid
        @NotNull
        ClientInfoDto clientInfoDto,

        @Valid
        @NotNull
        List<PaymentRowDto> paymentRowDtoList,

        @Valid
        @NotNull(message = "Document type must be provided")
        String documentType

) {
}
