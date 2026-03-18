package am.agro_trade.document_service.dto;

public record ClientInfoDto(

        String fullName,
        String email,
        String phoneNumber,
        PassportInfoDto passportInfo
        
) {
}
