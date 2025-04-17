package k_webtoons.k_webtoons.model.admin.common;

import k_webtoons.k_webtoons.security.AccountStatus;

import java.time.LocalDateTime;

public record UserDetailByAdminDTO(
        Long indexId,
        String userEmail,
        LocalDateTime createDateTime,
        AccountStatus accountStatus,
        Integer userAge,
        String gender,
        String nickname,
        String phoneNumber,
        String securityQuestion
        ) {
}
