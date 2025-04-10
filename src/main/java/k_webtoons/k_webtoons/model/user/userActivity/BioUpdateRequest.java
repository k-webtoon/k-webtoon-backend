package k_webtoons.k_webtoons.model.user.userActivity;

import jakarta.validation.constraints.NotBlank;

public record BioUpdateRequest(
        @NotBlank String bio
) {
}
