package k_webtoons.k_webtoons.model.auth.dto;

public record UserRegisterDTO(
        String userEmail,
        String userPassword,
        Integer userAge,
        String gender,
        String nickname,
        String phoneNumber,
        String securityQuestion,
        String securityAnswer
) {
}
