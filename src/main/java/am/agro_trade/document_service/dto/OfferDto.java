package am.agro_trade.document_service.dto;

import java.math.BigDecimal;

public record OfferDto(

        String offerType,
        BigDecimal interestRate
) {
}
