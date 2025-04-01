package k_webtoons.k_webtoons.model.user;

public record UserInfoDTO(
        Long indexId,
        String userEmail,
        String nickname,
        Integer userAge,
        String gender,
        Long commentCount,
        Long followerCount,
        Long followeeCount
) {
}
