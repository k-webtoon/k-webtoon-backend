package k_webtoons.k_webtoons.repository.userFollower;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user_follow.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    boolean existsByFollowerAndFollowee(AppUser follower, AppUser followee);

    Optional<UserFollow> findByFollowerAndFollowee(AppUser follower, AppUser followee);

    List<UserFollow> findByFollower(AppUser follower);

    List<UserFollow> findByFollowee(AppUser followee);

    void deleteByFollowerAndFollowee(AppUser follower, AppUser followee);

    long countByFollowee(AppUser followee);

    long countByFollower(AppUser follower);
}
