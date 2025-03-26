package k_webtoons.k_webtoons.model.user;

public record ChangePasswordRequest(
        String userEmail,
        String phoneNumber,
        String securityQuestion,
        String securityAnswer,
        String newPassword
) {

}
