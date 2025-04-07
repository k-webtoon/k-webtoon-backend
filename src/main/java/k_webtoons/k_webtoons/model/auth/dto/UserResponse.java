package k_webtoons.k_webtoons.model.auth.dto;

public record UserResponse(
        Long indexId,
        String userEmail,
        String nickname
) {
}
