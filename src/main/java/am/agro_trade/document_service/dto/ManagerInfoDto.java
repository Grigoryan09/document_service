package am.agro_trade.document_service.dto;

public record ManagerInfoDto(
        String fullName,
        String address,
        String email,
        String phoneNumber
) {
}