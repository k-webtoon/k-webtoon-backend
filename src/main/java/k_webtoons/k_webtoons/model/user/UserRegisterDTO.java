package k_webtoons.k_webtoons.model.user;

public record UserRegisterDTO(
        String userEmail,
        String userPassword,
        Integer userAge,
        String gender,
        String nickname
) {
}
