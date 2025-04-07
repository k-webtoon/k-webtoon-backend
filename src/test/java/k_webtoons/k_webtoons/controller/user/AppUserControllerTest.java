package k_webtoons.k_webtoons.controller.user;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.*;
import k_webtoons.k_webtoons.model.user_follow.FollowUserDTO;
import k_webtoons.k_webtoons.security.HeaderValidator;
import k_webtoons.k_webtoons.service.user.AppUserService;
import k_webtoons.k_webtoons.service.user.UserFollowService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserControllerTest {

    @Mock
    private AppUserService userService;

    @Mock
    private UserFollowService userFollowService;

    @Mock
    private HeaderValidator headerValidator;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AppUserController appUserController;

    @Test
    @DisplayName("사용자 정보 조회 테스트")
    void getUserInfoTest() {
        // Given
        Long userId = 1L;
        UserInfoDTO mockUserInfo = new UserInfoDTO(
                userId,
                "test@example.com",
                "테스트유저",
                25,
                "남성",
                5L,
                10L,
                7L
        );

        when(userService.getUserInfoByUserId(userId)).thenReturn(mockUserInfo);

        // When
        ResponseEntity<UserInfoDTO> response = appUserController.getUserInfo(userId);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockUserInfo);
        verify(userService, times(1)).getUserInfoByUserId(userId);
    }

    @Test
    @DisplayName("사용자 댓글 조회 테스트")
    void getCommentsTest() {
        // Given
        Long userId = 1L;
        List<UserCommentResponseDTO> mockComments = List.of(
                new UserCommentResponseDTO(1L, "좋은 웹툰이네요", "테스트유저", LocalDateTime.now(), 3)
        );

        when(userService.getCommentsByUserId(userId)).thenReturn(mockComments);

        // When
        ResponseEntity<List<UserCommentResponseDTO>> response = appUserController.getComments(userId);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockComments);
        verify(userService, times(1)).getCommentsByUserId(userId);
    }

    @Test
    @DisplayName("사용자가 좋아요한 웹툰 조회 테스트")
    void getLikedWebtoonsByUserIdTest() {
        // Given
        Long userId = 1L;
        List<LikeWebtoonDTO> mockLikedWebtoons = List.of(
                new LikeWebtoonDTO(1L, "인기 웹툰", "thumbnail.jpg")
        );

        when(userService.getLikedWebtoonsByUserId(userId)).thenReturn(mockLikedWebtoons);

        // When
        ResponseEntity<List<LikeWebtoonDTO>> response = appUserController.getLikedWebtoonsByUserId(userId);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockLikedWebtoons);
        verify(userService, times(1)).getLikedWebtoonsByUserId(userId);
    }

    @Test
    @DisplayName("팔로이 목록 조회 테스트")
    void getFolloweesTest() {
        // Given
        Long userId = 1L;
        List<FollowUserDTO> mockFollowees = List.of(
                new FollowUserDTO(2L, "test@test.com", "팔로우유저", 20, "남자")
        );

        when(userFollowService.getFollowees(userId)).thenReturn(mockFollowees);

        // When
        ResponseEntity<List<FollowUserDTO>> response = appUserController.getFollowees(userId);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockFollowees);
        verify(userFollowService, times(1)).getFollowees(userId);
    }

    @Test
    @DisplayName("팔로워 목록 조회 테스트")
    void getFollowersTest() {
        // Given
        Long userId = 1L;
        List<FollowUserDTO> mockFollowers = List.of(
                new FollowUserDTO(3L, "zxcv@zxcv.com", "팔로워유저", 22, "남자")
        );

        when(userFollowService.getFollowers(userId)).thenReturn(mockFollowers);

        // When
        ResponseEntity<List<FollowUserDTO>> response = appUserController.getFollowers(userId);

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(mockFollowers);
        verify(userFollowService, times(1)).getFollowers(userId);
    }

    @Test
    @DisplayName("현재 로그인한 사용자 정보 조회 테스트")
    void getCurrentUserInfoTest() {
        // Given
        Long userId = 1L;
        AppUser mockUser = new AppUser();
        mockUser.setIndexId(userId);
        mockUser.setUserEmail("test@example.com");
        mockUser.setNickname("테스트유저");
        mockUser.setRole("USER");

        UserInfoDTO mockUserInfo = new UserInfoDTO(
                userId,
                "test@example.com",
                "테스트유저",
                25,
                "남성",
                5L,
                10L,
                7L
        );

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        Collection<GrantedAuthority> authorities = List.of(authority);

        doReturn(authorities).when(authentication).getAuthorities();
        when(headerValidator.getAuthenticatedUser()).thenReturn(mockUser);
        when(userService.getUserInfoByUserId(userId)).thenReturn(mockUserInfo);

        // When
        ResponseEntity<MyInfoDTO> response = appUserController.getCurrentUserInfo();

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody().indexId()).isEqualTo(userId);
        assertThat(response.getBody().role()).isEqualTo("USER");
        assertThat(response.getBody().userEmail()).isEqualTo("test@example.com");
    }
}
