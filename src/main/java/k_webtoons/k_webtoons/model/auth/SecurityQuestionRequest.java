package k_webtoons.k_webtoons.model.auth;

public record SecurityQuestionRequest(
        String phoneNumber,
        String securityQuestion,
        String securityAnswer
) {
}
