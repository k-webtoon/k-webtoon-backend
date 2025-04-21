package k_webtoons.k_webtoons.service.user;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.*;
import k_webtoons.k_webtoons.model.webtoon.UserWebtoonReview;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import k_webtoons.k_webtoons.repository.webtoon.UserWebtoonReviewRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import k_webtoons.k_webtoons.service.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserFollowService userFollowService;

    @Mock
    private WebtoonCommentRepository webtoonCommentRepository;

    @Mock
    private UserWebtoonReviewRepository userWebtoonReviewRepository;

    @InjectMocks
    private AppUserService appUserService;

    // 테스트용 사용자 생성 메서드
    private AppUser createTestUser() {
        AppUser user = new AppUser();
        user.setIndexId(1L);
        user.setNickname("테스트유저");
        user.setWebtoonComments(Collections.emptyList()); // null 방지 초기화
        return user;
    }

    @Test
    @DisplayName("사용자 정보 조회 - 성공")
    void 사용자_정보_조회_성공() {
        // Given
        AppUser user = createTestUser();
        when(authService.getUserByUserIdNotAdmin(1L)).thenReturn(user);
        when(userFollowService.getFollowerCount(1L)).thenReturn(100L);
        when(userFollowService.getFolloweeCount(1L)).thenReturn(50L);

        // When
        UserInfoDTO result = appUserService.getUserInfoByUserId(1L);

        // Then
        assertEquals("테스트유저", result.nickname());
        assertEquals(100L, result.followerCount());
    }

    @Test
    @DisplayName("사용자 정보 조회 실패 - 사용자 없음")
    void 사용자_정보_조회_실패() {
        when(authService.getUserByUserIdNotAdmin(1L))
                .thenThrow(new CustomException("사용자 없음", "USER_NOT_FOUND"));

        assertThrows(CustomException.class, () ->
                appUserService.getUserInfoByUserId(1L));
    }

    @Test
    @DisplayName("댓글 조회 - 삭제된 댓글 제외")
    void 댓글_조회_삭제된_댓글_제외() {
        // Given
        AppUser user = createTestUser();
        when(authService.getUserByUserIdNotAdmin(1L)).thenReturn(user);

        WebtoonComment activeComment = new WebtoonComment();
        activeComment.setAppUser(user); // ★ AppUser 할당

        WebtoonComment deletedComment = new WebtoonComment();
        deletedComment.setDeletedDateTime(LocalDateTime.now());
        deletedComment.setAppUser(user); // (필요시)

        when(webtoonCommentRepository.findByUserIdAndDeletedDateTimeIsNull(1L))
                .thenReturn(List.of(activeComment));

        // When
        List<UserCommentResponseDTO> results = appUserService.getCommentsByUserId(1L);

        // Then
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("좋아요 웹툰 조회 - 성공")
    void 좋아요_웹툰_조회_성공() {
        // Given
        AppUser user = createTestUser();
        when(authService.getUserByUserIdNotAdmin(1L)).thenReturn(user);

        UserWebtoonReview review = new UserWebtoonReview();
        Webtoon webtoon = new Webtoon();
        webtoon.setId(1L);
        webtoon.setTitleName("테스트웹툰");
        webtoon.setThumbnailUrl("thumb.jpg");
        review.setWebtoon(webtoon); // Webtoon 객체 주입

        when(userWebtoonReviewRepository.findLikedWebtoonsByUserId(1L))
                .thenReturn(List.of(review));

        // When
        List<LikeWebtoonDTO> results = appUserService.getLikedWebtoonsByUserId(1L);

        // Then
        assertEquals("테스트웹툰", results.get(0).title());
    }

    @Test
    @DisplayName("좋아요 웹툰 조회 - 결과 없음")
    void 좋아요_웹툰_조회_결과_없음() {
        // Given
        AppUser user = createTestUser();
        when(authService.getUserByUserIdNotAdmin(1L)).thenReturn(user);

        when(userWebtoonReviewRepository.findLikedWebtoonsByUserId(1L))
                .thenReturn(Collections.emptyList());

        // When & Then
        assertTrue(appUserService.getLikedWebtoonsByUserId(1L).isEmpty());
    }
}