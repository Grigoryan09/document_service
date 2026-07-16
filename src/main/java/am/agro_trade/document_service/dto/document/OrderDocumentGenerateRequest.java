package am.agro_trade.document_service.dto.document;

import am.agro_trade.document_service.dto.BuyerInfoDto;
import am.agro_trade.document_service.dto.ManagerInfoDto;
import am.agro_trade.document_service.dto.ProductDto;
import am.agro_trade.document_service.dto.SellerInfoDto;

public record OrderDocumentGenerateRequest(

        long orderId,
        String createdAt,

        BuyerInfoDto buyerInfoDto,
        SellerInfoDto sellerInfoDto,
        ManagerInfoDto managerInfoDto,
        ProductDto productDto,

        long quantity,
        String totalPrice,

        long chatId,
        long senderUserId
) {
}