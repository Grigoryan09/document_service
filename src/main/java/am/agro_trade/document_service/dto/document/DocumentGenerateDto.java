package am.agro_trade.document_service.dto.document;

import am.agro_trade.document_service.dto.*;

import java.util.List;

public record DocumentGenerateDto(

        BankDto bankDto,
        OfferDto offerDto,
        FinalContractDto finalContractDto,
        ClientInfoDto clientInfoDto,
        List<PaymentRowDto> paymentRowDtoList,
        String documentType
) {
}
