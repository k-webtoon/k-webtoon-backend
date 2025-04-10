package k_webtoons.k_webtoons.model.admin;

import java.time.LocalDateTime;

public record FindAllUserByAdminDTO(
        Long indexId,
        String userEmail,
        String accountStatus,
        LocalDateTime createDateTime
        ) {
}
