package k_webtoons.k_webtoons.model.auth.dto;

import jakarta.validation.constraints.NotNull;

public record AccountStatusRequest(
        @NotNull String email
) {

}
