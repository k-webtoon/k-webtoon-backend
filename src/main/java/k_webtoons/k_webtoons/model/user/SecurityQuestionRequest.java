package k_webtoons.k_webtoons.model.user;

public record SecurityQuestionRequest(
        String phoneNumber,
        String securityQuestion,
        String securityAnswer
) {
}
