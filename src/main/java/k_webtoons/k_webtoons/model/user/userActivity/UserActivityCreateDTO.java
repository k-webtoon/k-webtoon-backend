package k_webtoons.k_webtoons.model.user.userActivity;

public record UserActivityCreateDTO(
        String profileImagePath,
        String bio,
        Boolean isProfilePublic
) {
}
