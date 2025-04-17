//package k_webtoons.k_webtoons.service.user;
//
//import jakarta.persistence.EntityNotFoundException;
//import k_webtoons.k_webtoons.exception.CustomException;
//import k_webtoons.k_webtoons.model.auth.AppUser;
//import k_webtoons.k_webtoons.model.user_follow.FollowUserDTO;
//import k_webtoons.k_webtoons.model.user_follow.UserFollow;
//import k_webtoons.k_webtoons.repository.user.UserRepository;
//import k_webtoons.k_webtoons.repository.userFollower.UserFollowRepository;
//import k_webtoons.k_webtoons.security.HeaderValidator;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserFollowServiceTest {
//
//    @Mock
//    private UserFollowRepository userFollowRepository;
//
//    @Mock
//    private UserRepository appUserRepository;
//
//    @Mock
//    private HeaderValidator headerValidator;
//
//    @InjectMocks
//    private UserFollowService userFollowService;
//
//    @Test
//    @DisplayName("팔로우 성공 테스트")
//    void followSuccessTest() {
//        // Given
//        Long followerId = 1L;
//        Long followeeId = 2L;
//
//        AppUser follower = new AppUser();
//        follower.setIndexId(followerId);
//        follower.setUserEmail("follower@example.com");
//        follower.setNickname("팔로워");
//
//        AppUser followee = new AppUser();
//        followee.setIndexId(followeeId);
//        followee.setUserEmail("followee@example.com");
//        followee.setNickname("팔로이");
//
//        when(headerValidator.getAuthenticatedUser()).thenReturn(follower);
//        when(appUserRepository.findById(followeeId)).thenReturn(Optional.of(followee));
//        when(userFollowRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(false);
//        when(userFollowRepository.save(any(UserFollow.class))).thenAnswer(i -> i.getArgument(0));
//
//        // When
//        userFollowService.follow(followeeId);
//
//        // Then
//        verify(headerValidator, times(1)).getAuthenticatedUser();
//        verify(appUserRepository, times(1)).findById(followeeId);
//        verify(userFollowRepository, times(1)).existsByFollowerAndFollowee(follower, followee);
//        verify(userFollowRepository, times(1)).save(any(UserFollow.class));
//    }
//
//    @Test
//    @DisplayName("자기 자신을 팔로우할 수 없는 테스트")
//    void followSelfTest() {
//        // Given
//        Long userId = 1L;
//
//        AppUser user = new AppUser();
//        user.setIndexId(userId);
//
//        when(headerValidator.getAuthenticatedUser()).thenReturn(user);
//
//        // When, Then
//        assertThrows(CustomException.class, () -> userFollowService.follow(userId));
//        verify(headerValidator, times(1)).getAuthenticatedUser();
//        verify(appUserRepository, never()).findById(any());
//        verify(userFollowRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("이미 팔로우한 경우 중복 팔로우 방지 테스트")
//    void followDuplicateTest() {
//        // Given
//        Long followerId = 1L;
//        Long followeeId = 2L;
//
//        AppUser follower = new AppUser();
//        follower.setIndexId(followerId);
//
//        AppUser followee = new AppUser();
//        followee.setIndexId(followeeId);
//
//        when(headerValidator.getAuthenticatedUser()).thenReturn(follower);
//        when(appUserRepository.findById(followeeId)).thenReturn(Optional.of(followee));
//        when(userFollowRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(true);
//
//        // When
//        userFollowService.follow(followeeId);
//
//        // Then
//        verify(headerValidator, times(1)).getAuthenticatedUser();
//        verify(appUserRepository, times(1)).findById(followeeId);
//        verify(userFollowRepository, times(1)).existsByFollowerAndFollowee(follower, followee);
//        verify(userFollowRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("언팔로우 성공 테스트")
//    void unfollowSuccessTest() {
//        // Given
//        Long followerId = 1L;
//        Long followeeId = 2L;
//
//        AppUser follower = new AppUser();
//        follower.setIndexId(followerId);
//
//        AppUser followee = new AppUser();
//        followee.setIndexId(followeeId);
//
//        when(headerValidator.getAuthenticatedUser()).thenReturn(follower);
//        when(appUserRepository.findById(followeeId)).thenReturn(Optional.of(followee));
//        when(userFollowRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(true);
//        doNothing().when(userFollowRepository).deleteByFollowerAndFollowee(follower, followee);
//
//        // When
//        userFollowService.unfollow(followeeId);
//
//        // Then
//        verify(headerValidator, times(1)).getAuthenticatedUser();
//        verify(appUserRepository, times(1)).findById(followeeId);
//        verify(userFollowRepository, times(1)).existsByFollowerAndFollowee(follower, followee);
//        verify(userFollowRepository, times(1)).deleteByFollowerAndFollowee(follower, followee);
//    }
//
//    @Test
//    @DisplayName("팔로워 목록 조회 테스트")
//    void getFollowersTest() {
//        // Given
//        Long userId = 1L;
//
//        AppUser user = new AppUser();
//        user.setIndexId(userId);
//
//        AppUser follower1 = new AppUser();
//        follower1.setIndexId(2L);
//        follower1.setUserEmail("follower1@example.com");
//        follower1.setNickname("팔로워1");
//        follower1.setUserAge(25);
//        follower1.setGender("남성");
//
//        AppUser follower2 = new AppUser();
//        follower2.setIndexId(3L);
//        follower2.setUserEmail("follower2@example.com");
//        follower2.setNickname("팔로워2");
//        follower2.setUserAge(30);
//        follower2.setGender("여성");
//
//        UserFollow follow1 = new UserFollow();
//        follow1.setFollower(follower1);
//        follow1.setFollowee(user);
//
//        UserFollow follow2 = new UserFollow();
//        follow2.setFollower(follower2);
//        follow2.setFollowee(user);
//
//        List<UserFollow> follows = new ArrayList<>();
//        follows.add(follow1);
//        follows.add(follow2);
//
//        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(userFollowRepository.findByFollowee(user)).thenReturn(follows);
//
//        // When
//        List<FollowUserDTO> followers = userFollowService.getFollowers(userId);
//
//        // Then
//        assertThat(followers).hasSize(2);
//
//        assertThat(followers.get(0).indexId()).isEqualTo(2L);
//        assertThat(followers.get(0).userEmail()).isEqualTo("follower1@example.com");
//        assertThat(followers.get(0).nickname()).isEqualTo("팔로워1");
//
//        assertThat(followers.get(1).indexId()).isEqualTo(3L);
//        assertThat(followers.get(1).userEmail()).isEqualTo("follower2@example.com");
//        assertThat(followers.get(1).nickname()).isEqualTo("팔로워2");
//
//        verify(appUserRepository, times(1)).findById(userId);
//        verify(userFollowRepository, times(1)).findByFollowee(user);
//    }
//
//    @Test
//    @DisplayName("팔로이 목록 조회 테스트")
//    void getFolloweesTest() {
//        // Given
//        Long userId = 1L;
//
//        AppUser user = new AppUser();
//        user.setIndexId(userId);
//
//        AppUser followee1 = new AppUser();
//        followee1.setIndexId(2L);
//        followee1.setUserEmail("followee1@example.com");
//        followee1.setNickname("팔로이1");
//        followee1.setUserAge(25);
//        followee1.setGender("남성");
//
//        AppUser followee2 = new AppUser();
//        followee2.setIndexId(3L);
//        followee2.setUserEmail("followee2@example.com");
//        followee2.setNickname("팔로이2");
//        followee2.setUserAge(30);
//        followee2.setGender("여성");
//
//        UserFollow follow1 = new UserFollow();
//        follow1.setFollower(user);
//        follow1.setFollowee(followee1);
//
//        UserFollow follow2 = new UserFollow();
//        follow2.setFollower(user);
//        follow2.setFollowee(followee2);
//
//        List<UserFollow> follows = new ArrayList<>();
//        follows.add(follow1);
//        follows.add(follow2);
//
//        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(userFollowRepository.findByFollower(user)).thenReturn(follows);
//
//        // When
//        List<FollowUserDTO> followees = userFollowService.getFollowees(userId);
//
//        // Then
//        assertThat(followees).hasSize(2);
//
//        assertThat(followees.get(0).indexId()).isEqualTo(2L);
//        assertThat(followees.get(0).userEmail()).isEqualTo("followee1@example.com");
//        assertThat(followees.get(0).nickname()).isEqualTo("팔로이1");
//
//        assertThat(followees.get(1).indexId()).isEqualTo(3L);
//        assertThat(followees.get(1).userEmail()).isEqualTo("followee2@example.com");
//        assertThat(followees.get(1).nickname()).isEqualTo("팔로이2");
//
//        verify(appUserRepository, times(1)).findById(userId);
//        verify(userFollowRepository, times(1)).findByFollower(user);
//    }
//
//    @Test
//    @DisplayName("팔로워 수 조회 테스트")
//    void getFollowerCountTest() {
//        // Given
//        Long userId = 1L;
//        AppUser user = new AppUser();
//        user.setIndexId(userId);
//
//        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(userFollowRepository.countByFollowee(user)).thenReturn(5L);
//
//        // When
//        long followerCount = userFollowService.getFollowerCount(userId);
//
//        // Then
//        assertThat(followerCount).isEqualTo(5L);
//        verify(appUserRepository, times(1)).findById(userId);
//        verify(userFollowRepository, times(1)).countByFollowee(user);
//    }
//
//    @Test
//    @DisplayName("팔로이 수 조회 테스트")
//    void getFolloweeCountTest() {
//        // Given
//        Long userId = 1L;
//        AppUser user = new AppUser();
//        user.setIndexId(userId);
//
//        when(appUserRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(userFollowRepository.countByFollower(user)).thenReturn(3L);
//
//        // When
//        long followeeCount = userFollowService.getFolloweeCount(userId);
//
//        // Then
//        assertThat(followeeCount).isEqualTo(3L);
//        verify(appUserRepository, times(1)).findById(userId);
//        verify(userFollowRepository, times(1)).countByFollower(user);
//    }
//}