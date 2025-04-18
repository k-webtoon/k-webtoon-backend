package k_webtoons.k_webtoons.repository.user;

import k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.*;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.security.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

// 소원 추가
    @Query("SELECT COUNT(u) FROM AppUser u")
    long countTotalUsers();

    @Query("SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.UserStatusRatioDto(u.accountStatus, COUNT(u)) " +
            "FROM AppUser u GROUP BY u.accountStatus")
    List<UserStatusRatioDto> countByStatus();

//    @Query("SELECT COUNT(u) FROM AppUser u WHERE u.lastActivityAt < :cutoff")
//    long countInactiveSince(LocalDate cutoff);
//
//    @Query("SELECT COUNT(DISTINCT u.id) FROM AppUser u WHERE u.lastActivityAt >= :cutoff")
//    long countActiveSince(LocalDate cutoff);

    @Query(value = """
                SELECT 
                    TO_CHAR(u.create_date_time, 'YYYY-MM-DD') AS date,
                    COUNT(*) AS count
                FROM app_user u
                WHERE u.create_date_time >= :startDate
                GROUP BY TO_CHAR(u.create_date_time, 'YYYY-MM-DD')
                ORDER BY TO_CHAR(u.create_date_time, 'YYYY-MM-DD')
            """, nativeQuery = true)
    List<DailySignupDto> getSignupCountsLast30Days(@Param("startDate") LocalDateTime startDate);


    @Query("SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.AgeDistributionDto(" +
            "CASE " +
            "  WHEN u.userAge BETWEEN 15 AND 21 THEN 1 " +
            "  WHEN u.userAge BETWEEN 22 AND 28 THEN 2 " +
            "  WHEN u.userAge BETWEEN 29 AND 35 THEN 3 " +
            "  ELSE 4 " +
            "END, COUNT(u)) " +
            "FROM AppUser u " +
            "GROUP BY " +
            "CASE " +
            "  WHEN u.userAge BETWEEN 15 AND 21 THEN 1 " +
            "  WHEN u.userAge BETWEEN 22 AND 28 THEN 2 " +
            "  WHEN u.userAge BETWEEN 29 AND 35 THEN 3 " +
            "  ELSE 4 " +
            "END")
    List<AgeDistributionDto> countByAgeGroup();

    @Query("SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.GenderRatioDto(u.gender, COUNT(u)) " +
            "FROM AppUser u GROUP BY u.gender")
    List<GenderRatioDto> countByGender();

    @Query("SELECT new k_webtoons.k_webtoons.model.admin.status.user_stats_dtos.GenderAgeActivityDto(u.gender, u.userAge, COUNT(u)) " +
            "FROM AppUser u GROUP BY u.gender, u.userAge")
    List<GenderAgeActivityDto> getGenderAgeActivity();


    // oauth2 조회용
    Optional<AppUser> findByUserEmailAndProvider(String email, String provider);
}
