package am.agro_trade.document_service.dto;

public record SellerInfoDto(
        String fullName,
        String address,
        String email,
        String phoneNumber
) {
}