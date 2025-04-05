package k_webtoons.k_webtoons.service.user;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.user.LikeWebtoonDTO;
import k_webtoons.k_webtoons.model.user.UserCommentResponseDTO;
import k_webtoons.k_webtoons.model.user.UserInfoDTO;
import k_webtoons.k_webtoons.model.webtoon.LikeWebtoonList;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoonComment.CommentLike;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.repository.webtoon.LikeWebtoonListRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import k_webtoons.k_webtoons.service.auth.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserFollowService userFollowService;

    @Mock
    private AuthService authService;

    @Mock
    private WebtoonCommentRepository webtoonCommentRepository;

    @Mock
    private LikeWebtoonListRepository likeWebtoonListRepository;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    @DisplayName("사용자 정보를 성공적으로 조회")
    void getUserInfoByUserIdSuccess() {
        // Given
        Long userId = 1L;
        AppUser mockUser = new AppUser();
        mockUser.setIndexId(userId);
        mockUser.setUserEmail("test@example.com");
        mockUser.setNickname("테스트유저");
        mockUser.setUserAge(25);
        mockUser.setGender("남성");
        mockUser.setWebtoonComments(new ArrayList<>());

        when(authService.getUserByUserIdNotAdmin(userId)).thenReturn(mockUser);
        when(userFollowService.getFollowerCount(userId)).thenReturn(10L);
        when(userFollowService.getFolloweeCount(userId)).thenReturn(5L);

        // When
        UserInfoDTO result = appUserService.getUserInfoByUserId(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.indexId()).isEqualTo(userId);
        assertThat(result.userEmail()).isEqualTo("test@example.com");
        assertThat(result.nickname()).isEqualTo("테스트유저");
        assertThat(result.userAge()).isEqualTo(25);
        assertThat(result.gender()).isEqualTo("남성");
        assertThat(result.commentCount()).isEqualTo(0);
        assertThat(result.followerCount()).isEqualTo(10L);
        assertThat(result.followeeCount()).isEqualTo(5L);

        verify(authService, times(1)).getUserByUserIdNotAdmin(userId);
        verify(userFollowService, times(1)).getFollowerCount(userId);
        verify(userFollowService, times(1)).getFolloweeCount(userId);
    }

    @Test
    @DisplayName("사용자 정보 조회 실패 - 예외 발생")
    void getUserInfoByUserIdFailure() {
        // Given
        Long userId = 1L;
        when(authService.getUserByUserIdNotAdmin(userId)).thenThrow(new RuntimeException("사용자를 찾을 수 없습니다."));

        // When, Then
        assertThrows(CustomException.class, () -> appUserService.getUserInfoByUserId(userId));
        verify(authService, times(1)).getUserByUserIdNotAdmin(userId);
    }

    @Test
    @DisplayName("사용자 댓글 목록 조회 성공")
    void getCommentsByUserIdSuccess() {
        // Given
        Long userId = 1L;
        AppUser mockUser = new AppUser();
        mockUser.setIndexId(userId);
        mockUser.setNickname("테스트유저");
        
        LocalDateTime createdDateTime = LocalDateTime.of(2023, 4, 1, 10, 0);
        
        WebtoonComment comment = new WebtoonComment();
        comment.setId(1L);
        comment.setContent("좋은 웹툰이네요");
        comment.setAppUser(mockUser);
        comment.setCreatedDate(createdDateTime);
        comment.setLikes(new ArrayList<>());

        when(authService.getUserByUserIdNotAdmin(userId)).thenReturn(mockUser);
        when(webtoonCommentRepository.findByUserIdAndDeletedDateTimeIsNull(userId)).thenReturn(List.of(comment));

        // When
        List<UserCommentResponseDTO> results = appUserService.getCommentsByUserId(userId);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo(1L);
        assertThat(results.get(0).content()).isEqualTo("좋은 웹툰이네요");
        assertThat(results.get(0).nickname()).isEqualTo("테스트유저");
        assertThat(results.get(0).createdDate()).isEqualTo(createdDateTime);
        assertThat(results.get(0).likeCount()).isEqualTo(0);

        verify(authService, times(1)).getUserByUserIdNotAdmin(userId);
        verify(webtoonCommentRepository, times(1)).findByUserIdAndDeletedDateTimeIsNull(userId);
    }

    @Test
    @DisplayName("사용자 댓글 목록 조회 - 빈 목록")
    void getCommentsByUserIdEmptyList() {
        // Given
        Long userId = 1L;
        AppUser mockUser = new AppUser();
        mockUser.setIndexId(userId);

        when(authService.getUserByUserIdNotAdmin(userId)).thenReturn(mockUser);
        when(webtoonCommentRepository.findByUserIdAndDeletedDateTimeIsNull(userId)).thenReturn(Collections.emptyList());

        // When
        List<UserCommentResponseDTO> results = appUserService.getCommentsByUserId(userId);

        // Then
        assertThat(results).isEmpty();
        verify(authService, times(1)).getUserByUserIdNotAdmin(userId);
        verify(webtoonCommentRepository, times(1)).findByUserIdAndDeletedDateTimeIsNull(userId);
    }

    @Test
    @DisplayName("사용자 댓글 목록 조회 실패 - 예외 발생")
    void getCommentsByUserIdFailure() {
        // Given
        Long userId = 1L;
        when(authService.getUserByUserIdNotAdmin(userId)).thenThrow(new RuntimeException("사용자를 찾을 수 없습니다."));

        // When, Then
        assertThrows(CustomException.class, () -> appUserService.getCommentsByUserId(userId));
        verify(authService, times(1)).getUserByUserIdNotAdmin(userId);
    }

    @Test
    @DisplayName("좋아요한 웹툰 목록 조회 성공")
    void getLikedWebtoonsByUserIdSuccess() {
        // Given
        Long userId = 1L;
        AppUser mockUser = new AppUser();
        mockUser.setIndexId(userId);

        Webtoon webtoon = new Webtoon();
        webtoon.setId(1L);
        webtoon.setTitleName("인기 웹툰");
        webtoon.setThumbnailUrl("thumbnail.jpg");

        LikeWebtoonList likeWebtoon = new LikeWebtoonList();
        likeWebtoon.setWebtoon(webtoon);
        likeWebtoon.setAppUser(mockUser);

        when(authService.getUserByUserIdNotAdmin(userId)).thenReturn(mockUser);
        when(likeWebtoonListRepository.findLikedWebtoonsByUserId(userId)).thenReturn(List.of(likeWebtoon));

        // When
        List<LikeWebtoonDTO> results = appUserService.getLikedWebtoonsByUserId(userId);

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).id()).isEqualTo(1L);
        assertThat(results.get(0).title()).isEqualTo("인기 웹툰");
        assertThat(results.get(0).thumbnailUrl()).isEqualTo("thumbnail.jpg");

        verify(authService, times(1)).getUserByUserIdNotAdmin(userId);
        verify(likeWebtoonListRepository, times(1)).findLikedWebtoonsByUserId(userId);
    }

    @Test
    @DisplayName("좋아요한 웹툰 목록 조회 실패 - 예외 발생")
    void getLikedWebtoonsByUserIdFailure() {
        // Given
        Long userId = 1L;
        when(authService.getUserByUserIdNotAdmin(userId)).thenThrow(new RuntimeException("사용자를 찾을 수 없습니다."));

        // When, Then
        assertThrows(CustomException.class, () -> appUserService.getLikedWebtoonsByUserId(userId));
        verify(authService, times(1)).getUserByUserIdNotAdmin(userId);
    }
} 