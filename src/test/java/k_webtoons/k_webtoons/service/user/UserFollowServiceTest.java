package k_webtoons.k_webtoons.service.user;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user_follow.FollowUserDTO;
import k_webtoons.k_webtoons.model.user_follow.UserFollow;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.userFollower.UserFollowRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFollowServiceTest {

    @Mock
    private UserFollowRepository userFollowRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HeaderValidator headerValidator;

    @InjectMocks
    private UserFollowService userFollowService;

    // 테스트용 AppUser 생성
    private AppUser createUser(Long id, String nickname) {
        AppUser user = new AppUser();
        user.setIndexId(id);
        user.setNickname(nickname);
        user.setUserEmail("test" + id + "@test.com");
        user.setUserAge(20 + id.intValue());
        user.setGender("남");
        return user;
    }

    @Test
    @DisplayName("팔로우 - 성공")
    void 팔로우_성공() {
        AppUser follower = createUser(1L, "팔로워");
        AppUser followee = createUser(2L, "팔로이");

        when(headerValidator.getAuthenticatedUser()).thenReturn(follower);
        when(userRepository.findById(2L)).thenReturn(Optional.of(followee));
        when(userFollowRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(false);

        userFollowService.follow(2L);

        verify(userFollowRepository).save(any(UserFollow.class));
    }

    @Test
    @DisplayName("팔로우 - 자기 자신 팔로우 시 예외")
    void 팔로우_자기자신_예외() {
        AppUser follower = createUser(1L, "팔로워");
        when(headerValidator.getAuthenticatedUser()).thenReturn(follower);

        CustomException ex = assertThrows(CustomException.class, () -> userFollowService.follow(1L));
        assertEquals("INVALID_REQUEST", ex.getErrorCode());
    }

    @Test
    @DisplayName("언팔로우 - 성공")
    void 언팔로우_성공() {
        AppUser follower = createUser(1L, "팔로워");
        AppUser followee = createUser(2L, "팔로이");

        when(headerValidator.getAuthenticatedUser()).thenReturn(follower);
        when(userRepository.findById(2L)).thenReturn(Optional.of(followee));
        when(userFollowRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(true);

        userFollowService.unfollow(2L);

        verify(userFollowRepository).deleteByFollowerAndFollowee(follower, followee);
    }

    @Test
    @DisplayName("언팔로우 - 팔로우 관계 없음 예외")
    void 언팔로우_관계없음_예외() {
        AppUser follower = createUser(1L, "팔로워");
        AppUser followee = createUser(2L, "팔로이");

        when(headerValidator.getAuthenticatedUser()).thenReturn(follower);
        when(userRepository.findById(2L)).thenReturn(Optional.of(followee));
        when(userFollowRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(false);

        CustomException ex = assertThrows(CustomException.class, () -> userFollowService.unfollow(2L));
        assertEquals("INVALID_REQUEST", ex.getErrorCode());
    }

    @Test
    @DisplayName("팔로워 목록 조회 - 성공")
    void 팔로워_목록_조회_성공() {
        AppUser user = createUser(2L, "팔로이");
        AppUser follower = createUser(1L, "팔로워");
        UserFollow userFollow = UserFollow.builder()
                .follower(follower)
                .followee(user)
                .followedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(userFollowRepository.findByFollowee(user)).thenReturn(List.of(userFollow));

        List<FollowUserDTO> result = userFollowService.getFollowers(2L);

        assertEquals(1, result.size());
        assertEquals("팔로워", result.get(0).nickname());
    }

    @Test
    @DisplayName("팔로잉 목록 조회 - 성공")
    void 팔로잉_목록_조회_성공() {
        AppUser user = createUser(1L, "팔로워");
        AppUser followee = createUser(2L, "팔로이");
        UserFollow userFollow = UserFollow.builder()
                .follower(user)
                .followee(followee)
                .followedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userFollowRepository.findByFollower(user)).thenReturn(List.of(userFollow));

        List<FollowUserDTO> result = userFollowService.getFollowees(1L);

        assertEquals(1, result.size());
        assertEquals("팔로이", result.get(0).nickname());
    }

    @Test
    @DisplayName("팔로워 수 조회 - 성공")
    void 팔로워_수_조회_성공() {
        AppUser user = createUser(2L, "팔로이");
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(userFollowRepository.countByFollowee(user)).thenReturn(3L);

        long count = userFollowService.getFollowerCount(2L);

        assertEquals(3L, count);
    }

    @Test
    @DisplayName("팔로잉 수 조회 - 성공")
    void 팔로잉_수_조회_성공() {
        AppUser user = createUser(1L, "팔로워");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userFollowRepository.countByFollower(user)).thenReturn(2L);

        long count = userFollowService.getFolloweeCount(1L);

        assertEquals(2L, count);
    }

    @Test
    @DisplayName("팔로우 상태 확인 - 성공")
    void 팔로우_상태_확인_성공() {
        AppUser follower = createUser(1L, "팔로워");
        AppUser followee = createUser(2L, "팔로이");
        when(userRepository.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepository.findById(2L)).thenReturn(Optional.of(followee));
        when(userFollowRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(true);

        boolean result = userFollowService.checkFollowStatus(1L, 2L);

        assertTrue(result);
    }

    @Test
    @DisplayName("팔로우 상태 확인 - 유저 없음 예외")
    void 팔로우_상태_유저없음_예외() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException ex = assertThrows(CustomException.class, () -> userFollowService.checkFollowStatus(1L, 2L));
        assertEquals("USER_NOT_FOUND", ex.getErrorCode());
    }
}
