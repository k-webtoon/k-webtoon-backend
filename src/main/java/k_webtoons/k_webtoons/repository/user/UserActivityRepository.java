package k_webtoons.k_webtoons.repository.user;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.userActivity.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

    Optional<UserActivity> findByAppUser(AppUser appUser);

    Optional<UserActivity> findByAppUser_indexId(Long userId);
}
