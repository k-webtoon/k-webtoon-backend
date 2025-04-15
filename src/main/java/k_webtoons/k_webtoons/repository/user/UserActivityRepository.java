package k_webtoons.k_webtoons.repository.user;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    Optional<UserActivity> findByAppUser(AppUser appUser);

    @Query("SELECT ua FROM UserActivity ua WHERE ua.appUser.indexId = :userId")
    Optional<UserActivity> findByUserId(@Param("userId") Long userId);
}
