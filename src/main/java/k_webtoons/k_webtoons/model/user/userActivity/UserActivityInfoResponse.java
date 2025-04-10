package k_webtoons.k_webtoons.model.user.userActivity;

public record UserActivityInfoResponse(
        String profileImageUrl,
        String bio
) {
    public UserActivityInfoResponse(UserActivity userActivity) {
        this(
                "/img/" + userActivity.getProfileImageUrl(), // ▶▶▶ 경로 수정 ◀◀◀
                userActivity.getBio()
        );
    }
}