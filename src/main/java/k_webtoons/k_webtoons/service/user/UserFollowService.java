package k_webtoons.k_webtoons.service.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user_follow.UserFollow;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.userFollower.UserFollowRepository;
import k_webtoons.k_webtoons.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFollowService {

    private final UserFollowRepository userFollowRepository;
    private final UserRepository appUserRepository;
    private final AuthService authService;

    public void follow(long followerId, long followeeId) {
        try {
            AppUser authenticatedUser = authService.getAuthenticatedUser();

            if (authenticatedUser.getIndexId() != followerId) {
                throw new CustomException("권한이 없습니다.", "UNAUTHORIZED");
            }

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
        } catch (Exception e) {
            throw new CustomException("팔로우 작업 중 오류가 발생했습니다.", "FOLLOW_ERROR");
        }
    }

    @Transactional
    public void unfollow(int followerId, int followeeId) {
        try {
            AppUser authenticatedUser = authService.getAuthenticatedUser();

            if (authenticatedUser.getIndexId() != followerId) {
                throw new CustomException("권한이 없습니다.", "UNAUTHORIZED");
            }

            AppUser follower = appUserRepository.findById((long) followerId)
                    .orElseThrow(() -> new EntityNotFoundException("팔로워 유저 없음"));
            AppUser followee = appUserRepository.findById((long) followeeId)
                    .orElseThrow(() -> new EntityNotFoundException("팔로이 유저 없음"));

            boolean exists = userFollowRepository.existsByFollowerAndFollowee(follower, followee);
            if (!exists) {
                throw new IllegalStateException("팔로우 관계가 존재하지 않습니다.");
            }

            userFollowRepository.deleteByFollowerAndFollowee(follower, followee);
        } catch (Exception e) {
            throw new CustomException("언팔로우 작업 중 오류가 발생했습니다.", "UNFOLLOW_ERROR");
        }
    }

    // 팔로워 목록 조회 (어드민 제외)
    public List<AppUser> getFollowers(long userId) {
        AppUser user = authService.getUserByUserIdNotAdmin(userId);
        return userFollowRepository.findByFollowee(user).stream()
                .map(UserFollow::getFollower)
                .collect(Collectors.toList());
    }

    // 팔로잉 목록 조회 (어드민 제외)
    public List<AppUser> getFollowees(long userId) {
        AppUser user = authService.getUserByUserIdNotAdmin(userId);
        return userFollowRepository.findByFollower(user).stream()
                .map(UserFollow::getFollowee)
                .collect(Collectors.toList());
    }

    // 팔로워 수 조회 (어드민 제외)
    public long getFollowerCount(long userId) {
        AppUser user = authService.getUserByUserIdNotAdmin(userId);
        return userFollowRepository.countByFollowee(user);
    }

    // 팔로잉 수 조회 (어드민 제외)
    public long getFolloweeCount(long userId) {
        AppUser user = authService.getUserByUserIdNotAdmin(userId);
        return userFollowRepository.countByFollower(user);
    }
}
