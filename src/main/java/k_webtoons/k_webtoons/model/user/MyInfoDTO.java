package k_webtoons.k_webtoons.model.user;

public record MyInfoDTO(
        Long indexId,
        String userEmail,
        String nickname,
        Integer userAge,
        String gender,
        Long commentCount,
        Long followerCount,
        Long followeeCount,
        String role
) {
}
