package k_webtoons.k_webtoons.model.auth;

public record ChangePasswordRequest(
        String userEmail,
        String phoneNumber,
        String securityQuestion,
        String securityAnswer,
        String newPassword
) {

}
