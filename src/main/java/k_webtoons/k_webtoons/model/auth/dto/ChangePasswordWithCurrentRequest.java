package k_webtoons.k_webtoons.model.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordWithCurrentRequest(
        @NotBlank String currentPassword,
        @NotBlank String newPassword,
        @NotBlank String confirmNewPassword

) {
}
