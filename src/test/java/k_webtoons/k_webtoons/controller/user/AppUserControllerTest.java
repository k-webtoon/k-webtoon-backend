package k_webtoons.k_webtoons.controller.user;

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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserControllerTest {

    @Mock
    private AppUserService userService;

    @Mock
    private UserFollowService userFollowService;

    @Mock
    private HeaderValidator headerValidator;

    @InjectMocks
    private AppUserController appUserController;

    @Test
    @DisplayName("사용자 정보 조회 - 성공")
    void 사용자_정보_조회_성공() {
        // Given
        UserInfoDTO mockUser = new UserInfoDTO(1L, "test@test.com", "닉네임", 25, "M", 10L, 100L, 50L);
        when(userService.getUserInfoByUserId(1L)).thenReturn(mockUser);

        // When
        ResponseEntity<UserInfoDTO> response = appUserController.getUserInfo(1L);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("닉네임", response.getBody().nickname());
    }

    @Test
    @DisplayName("사용자 댓글 조회 - 성공")
    void 사용자_댓글_조회_성공() {
        // Given
        UserCommentResponseDTO comment = new UserCommentResponseDTO(1L, "댓글내용", "닉네임", null, 5);
        when(userService.getCommentsByUserId(1L)).thenReturn(List.of(comment));

        // When
        ResponseEntity<List<UserCommentResponseDTO>> response = appUserController.getComments(1L);

        // Then
        assertEquals(1, response.getBody().size());
        assertEquals("댓글내용", response.getBody().get(0).content());
    }

    @Test
    @DisplayName("좋아요 웹툰 조회 - 성공")
    void 좋아요_웹툰_조회_성공() {
        // Given
        LikeWebtoonDTO webtoon = new LikeWebtoonDTO(1L, "웹툰제목", "thumb.jpg");
        when(userService.getLikedWebtoonsByUserId(1L)).thenReturn(List.of(webtoon));

        // When
        ResponseEntity<List<LikeWebtoonDTO>> response = appUserController.getLikedWebtoonsByUserId(1L);

        // Then
        assertEquals(1, response.getBody().size());
        assertEquals("웹툰제목", response.getBody().get(0).title());
    }

    @Test
    @DisplayName("팔로잉 목록 조회 - 성공")
    void 팔로잉_목록_조회_성공() {
        // Given
        FollowUserDTO followee = new FollowUserDTO(2L, "test@test.com", "팔로우유저",21,"남");
        when(userFollowService.getFollowees(1L)).thenReturn(List.of(followee));

        // When
        ResponseEntity<List<FollowUserDTO>> response = appUserController.getFollowees(1L);

        // Then
        assertEquals(1, response.getBody().size());
        assertEquals("팔로우유저", response.getBody().get(0).nickname());
    }

}