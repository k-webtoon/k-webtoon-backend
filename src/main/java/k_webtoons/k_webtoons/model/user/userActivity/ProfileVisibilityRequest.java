package k_webtoons.k_webtoons.model.user.userActivity;

import jakarta.validation.constraints.NotNull;

public record ProfileVisibilityRequest(
        @NotNull Boolean isProfilePublic
) {
}
