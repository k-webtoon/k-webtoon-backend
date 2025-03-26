package k_webtoons.k_webtoons.model.auth;

public record UserResponse(
        Long indexId,
        String userEmail,
        String nickname
) {
}
