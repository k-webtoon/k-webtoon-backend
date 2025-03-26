package k_webtoons.k_webtoons.service.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import k_webtoons.k_webtoons.model.user.AppUser;
import k_webtoons.k_webtoons.model.user_follow.UserFollow;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.userFollower.UserFollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFollowService {

    private final UserFollowRepository userFollowRepository;
    private final UserRepository appUserRepository;

    public void follow(long followerId, long followeeId) {
        if (followerId == followeeId) {
            throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
        }

        AppUser follower = appUserRepository.findById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("팔로워 유저 없음"));
        AppUser followee = appUserRepository.findById(followeeId)
                .orElseThrow(() -> new EntityNotFoundException("팔로이 유저 없음"));

        boolean exists = userFollowRepository.existsByFollowerAndFollowee(follower, followee);
        if (!exists) {
            UserFollow follow = UserFollow.builder()
                    .follower(follower)
                    .followee(followee)
                    .followedAt(LocalDateTime.now())
                    .build();
            userFollowRepository.save(follow);
        }
    }

    @Transactional
    public void unfollow(int followerId, int followeeId) {
        AppUser follower = appUserRepository.findById((long) followerId)
                .orElseThrow(() -> new EntityNotFoundException("팔로워 유저 없음"));
        AppUser followee = appUserRepository.findById((long) followeeId)
                .orElseThrow(() -> new EntityNotFoundException("팔로이 유저 없음"));

        boolean exists = userFollowRepository.existsByFollowerAndFollowee(follower, followee);
        if (!exists) {
            throw new IllegalStateException("팔로우 관계가 존재하지 않습니다.");
        }

        userFollowRepository.deleteByFollowerAndFollowee(follower, followee);
    }

    public List<AppUser> getFollowers(long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));

        return userFollowRepository.findByFollowee(user).stream()
                .map(UserFollow::getFollower)
                .collect(Collectors.toList());
    }

    public List<AppUser> getFollowees(long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));

        return userFollowRepository.findByFollower(user).stream()
                .map(UserFollow::getFollowee)
                .collect(Collectors.toList());
    }

    public long getFollowerCount(long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));

        return userFollowRepository.countByFollowee(user);
    }

    public long getFolloweeCount(long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("유저 없음"));

        return userFollowRepository.countByFollower(user);
    }
}
