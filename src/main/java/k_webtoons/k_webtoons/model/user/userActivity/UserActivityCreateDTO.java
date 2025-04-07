package k_webtoons.k_webtoons.model.user.userActivity;

public record UserActivityCreateDTO(
        byte[] profileImage,
        String bio,
        Boolean isProfilePublic
) {
}
