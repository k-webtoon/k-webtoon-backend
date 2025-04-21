package k_webtoons.k_webtoons.controller.user;

import k_webtoons.k_webtoons.controller.userFollow.UserFollowController;
import k_webtoons.k_webtoons.model.user_follow.FollowUserDTO;
import k_webtoons.k_webtoons.service.user.UserFollowService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFollowControllerTest {

    @Mock
    private UserFollowService userFollowService;

    @InjectMocks
    private UserFollowController userFollowController;

    @Test
    @DisplayName("팔로우 - 성공")
    void 팔로우_성공() {
        // When
        ResponseEntity<Void> response = userFollowController.follow(2L);

        // Then
        verify(userFollowService).follow(2L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("언팔로우 - 성공")
    void 언팔로우_성공() {
        // When
        ResponseEntity<Void> response = userFollowController.unfollow(2L);

        // Then
        verify(userFollowService).unfollow(2L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    @DisplayName("팔로워 목록 조회 - 성공")
    void 팔로워_목록_조회_성공() {
        // Given
        FollowUserDTO follower = new FollowUserDTO(1L, "test@test.com", "팔로워", 20, "남");
        when(userFollowService.getFollowers(1L)).thenReturn(List.of(follower));

        // When
        ResponseEntity<List<FollowUserDTO>> response = userFollowController.getFollowers(1L);

        // Then
        assertEquals(1, response.getBody().size());
        assertEquals("팔로워", response.getBody().get(0).nickname());
    }

    @Test
    @DisplayName("팔로잉 목록 조회 - 성공")
    void 팔로잉_목록_조회_성공() {
        // Given
        FollowUserDTO followee = new FollowUserDTO(2L, "test2@test.com", "팔로잉", 21, "여");
        when(userFollowService.getFollowees(1L)).thenReturn(List.of(followee));

        // When
        ResponseEntity<List<FollowUserDTO>> response = userFollowController.getFollowees(1L);

        // Then
        assertEquals(1, response.getBody().size());
        assertEquals("팔로잉", response.getBody().get(0).nickname());
    }

    @Test
    @DisplayName("팔로워 수 조회 - 성공")
    void 팔로워_수_조회_성공() {
        // Given
        when(userFollowService.getFollowerCount(1L)).thenReturn(5L);

        // When
        ResponseEntity<Long> response = userFollowController.getFollowerCount(1L);

        // Then
        assertEquals(5L, response.getBody());
    }

    @Test
    @DisplayName("팔로잉 수 조회 - 성공")
    void 팔로잉_수_조회_성공() {
        // Given
        when(userFollowService.getFolloweeCount(1L)).thenReturn(3L);

        // When
        ResponseEntity<Long> response = userFollowController.getFolloweeCount(1L);

        // Then
        assertEquals(3L, response.getBody());
    }

    @Test
    @DisplayName("팔로우 상태 확인 - 성공")
    void 팔로우_상태_확인_성공() {
        // Given
        when(userFollowService.checkFollowStatus(1L, 2L)).thenReturn(true);

        // When
        ResponseEntity<Boolean> response = userFollowController.checkFollowStatus(1L, 2L);

        // Then
        assertTrue(response.getBody());
    }
}