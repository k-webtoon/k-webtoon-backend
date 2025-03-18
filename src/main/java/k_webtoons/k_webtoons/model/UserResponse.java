package k_webtoons.k_webtoons.model;

public record UserResponse(
        Long indexId,
        String userEmail,
        String nickname
) {
}
