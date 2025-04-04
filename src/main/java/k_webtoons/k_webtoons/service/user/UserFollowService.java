package k_webtoons.k_webtoons.service.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user_follow.FollowUserDTO;
import k_webtoons.k_webtoons.model.user_follow.UserFollow;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.userFollower.UserFollowRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import k_webtoons.k_webtoons.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFollowService {

    private final UserFollowRepository userFollowRepository;
    private final UserRepository appUserRepository;
    private final HeaderValidator headerValidator; // HeaderValidator 주입

    @Transactional
    public void follow(long followeeId) {
        try {
            AppUser authenticatedUser = headerValidator.getAuthenticatedUser(); // 토큰에서 인증된 사용자 가져오기
            long followerId = authenticatedUser.getIndexId();

            if (followerId == followeeId) {
                throw new IllegalArgumentException("자기 자신은 팔로우할 수 없습니다.");
            }

            AppUser follower = authenticatedUser; // 인증된 사용자를 팔로워로 설정
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
        } catch (EntityNotFoundException e) {
            throw new CustomException("유저를 찾을 수 없습니다: " + e.getMessage(), "USER_NOT_FOUND");
        } catch (IllegalArgumentException e) {
            throw new CustomException("잘못된 요청: " + e.getMessage(), "INVALID_REQUEST");
        } catch (Exception e) {
            throw new CustomException("팔로우 작업 중 오류가 발생했습니다.", "FOLLOW_ERROR");
        }
    }

    @Transactional
    public void unfollow(long followeeId) {
        try {
            AppUser authenticatedUser = headerValidator.getAuthenticatedUser(); // 토큰에서 인증된 사용자 가져오기
            long followerId = authenticatedUser.getIndexId();

            AppUser follower = authenticatedUser; // 인증된 사용자를 팔로워로 설정
            AppUser followee = appUserRepository.findById(followeeId)
                    .orElseThrow(() -> new EntityNotFoundException("팔로이 유저 없음"));

            boolean exists = userFollowRepository.existsByFollowerAndFollowee(follower, followee);
            if (!exists) {
                throw new IllegalStateException("팔로우 관계가 존재하지 않습니다.");
            }

            userFollowRepository.deleteByFollowerAndFollowee(follower, followee);
        } catch (EntityNotFoundException e) {
            throw new CustomException("유저를 찾을 수 없습니다: " + e.getMessage(), "USER_NOT_FOUND");
        } catch (IllegalStateException e) {
            throw new CustomException("잘못된 요청: " + e.getMessage(), "INVALID_REQUEST");
        } catch (Exception e) {
            throw new CustomException("언팔로우 작업 중 오류가 발생했습니다.", "UNFOLLOW_ERROR");
        }
    }

    @Transactional(readOnly = true)
    public List<FollowUserDTO> getFollowers(long userId) {
        try {
            AppUser user = appUserRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

            return userFollowRepository.findByFollowee(user).stream()
                    .map(UserFollow::getFollower)
                    .map(follower -> new FollowUserDTO(
                            follower.getIndexId(),
                            follower.getUserEmail(),
                            follower.getNickname(),
                            follower.getUserAge(),
                            follower.getGender()
                    ))
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new CustomException("유저를 찾을 수 없습니다: " + e.getMessage(), "USER_NOT_FOUND");
        } catch (Exception e) {
            throw new CustomException("팔로워 목록 조회 중 오류가 발생했습니다.", "GET_FOLLOWERS_ERROR");
        }
    }

    @Transactional(readOnly = true)
    public List<FollowUserDTO> getFollowees(long userId) {
        try {
            AppUser user = appUserRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

            return userFollowRepository.findByFollower(user).stream()
                    .map(UserFollow::getFollowee)
                    .map(followee -> new FollowUserDTO(
                            followee.getIndexId(),
                            followee.getUserEmail(),
                            followee.getNickname(),
                            followee.getUserAge(),
                            followee.getGender()
                    ))
                    .collect(Collectors.toList());
        } catch (EntityNotFoundException e) {
            throw new CustomException("유저를 찾을 수 없습니다: " + e.getMessage(), "USER_NOT_FOUND");
        } catch (Exception e) {
            throw new CustomException("팔로잉 목록 조회 중 오류가 발생했습니다.", "GET_FOLLOWEES_ERROR");
        }
    }

    @Transactional(readOnly = true)
    public long getFollowerCount(long userId) {
        try {
            AppUser user = appUserRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

            return userFollowRepository.countByFollowee(user);
        } catch (EntityNotFoundException e) {
            throw new CustomException("유저를 찾을 수 없습니다: " + e.getMessage(), "USER_NOT_FOUND");
        } catch (Exception e) {
            throw new CustomException("팔로워 수 조회 중 오류가 발생했습니다.", "GET_FOLLOWER_COUNT_ERROR");
        }
    }

    @Transactional(readOnly = true)
    public long getFolloweeCount(long userId) {
        try {
            AppUser user = appUserRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

            return userFollowRepository.countByFollower(user);
        } catch (EntityNotFoundException e) {
            throw new CustomException("유저를 찾을 수 없습니다: " + e.getMessage(), "USER_NOT_FOUND");
        } catch (Exception e) {
            throw new CustomException("팔로잉 수 조회 중 오류가 발생했습니다.", "GET_FOLLOWEE_COUNT_ERROR");
        }
    }
}
