package k_webtoons.k_webtoons.model.admin.status.user_stats_dtos;

import k_webtoons.k_webtoons.security.AccountStatus;

public class UserStatusRatioDto {
    private AccountStatus accountStatus;
    private long count;

    public UserStatusRatioDto() {
        // 기본 생성자 (Hibernate용)
    }

    public UserStatusRatioDto(AccountStatus accountStatus, long count) {
        this.accountStatus = accountStatus;
        this.count = count;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public long getCount() {
        return count;
    }
}
