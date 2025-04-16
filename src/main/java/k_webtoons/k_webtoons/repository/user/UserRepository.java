package k_webtoons.k_webtoons.repository.user;

import k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.*;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.security.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUserEmail(String userEmail);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<AppUser> findByUserEmail(String userEmail);

    Optional<String> findRoleByUserEmail(String userEmail);

    Optional<AppUser> findByPhoneNumber(String phoneNumber);

    List<AppUser> findByAccountStatus(AccountStatus status);

    long countByAccountStatus(AccountStatus status);

    Page<AppUser> findByAccountStatus(AccountStatus status, Pageable pageable);


    //소원 추가
// 소원 추가
    @Query("SELECT COUNT(u) FROM AppUser u")
    long countTotalUsers();

    @Query("SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.UserStatusRatioDto(u.accountStatus, COUNT(u)) " +
            "FROM AppUser u GROUP BY u.accountStatus")
    List<UserStatusRatioDto> countByStatus();

    @Query("SELECT COUNT(u) FROM AppUser u WHERE u.lastActivityAt < :cutoff")
    long countInactiveSince(LocalDate cutoff);

    @Query("SELECT COUNT(DISTINCT u.id) FROM AppUser u WHERE u.lastActivityAt >= :cutoff")
    long countActiveSince(LocalDate cutoff);

    @Query("""
                SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.DailySignupDto(
                    FUNCTION('DATE', u.createDateTime), COUNT(u)
                )
                FROM AppUser u
                WHERE u.createDateTime >= :#{T(java.time.LocalDateTime).now().minusDays(30)}
                GROUP BY FUNCTION('DATE', u.createDateTime)
                ORDER BY FUNCTION('DATE', u.createDateTime)
            """)
    List<DailySignupDto> getSignupCountsLast30Days();

    @Query("SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.AgeDistributionDto(u.userAge, COUNT(u)) " +
            "FROM AppUser u GROUP BY u.userAge")
    List<AgeDistributionDto> countByAgeGroup();

    @Query("SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.GenderRatioDto(u.gender, COUNT(u)) " +
            "FROM AppUser u GROUP BY u.gender")
    List<GenderRatioDto> countByGender();

    @Query("SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.GenderAgeActivityDto(u.gender, u.userAge, COUNT(u)) " +
            "FROM AppUser u GROUP BY u.gender, u.userAge")
    List<GenderAgeActivityDto> getGenderAgeActivity();


}
