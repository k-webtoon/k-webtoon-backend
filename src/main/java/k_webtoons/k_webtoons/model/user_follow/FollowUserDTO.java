package k_webtoons.k_webtoons.model.user_follow;

public record FollowUserDTO(
        Long indexId,
        String userEmail,
        String nickname,
        Integer userAge,
        String gender
) {
}
