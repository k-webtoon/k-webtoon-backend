package k_webtoons.k_webtoons.service.webtoonComment;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoonComment.CommentLike;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentRequestDTO;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentResponseDTO;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentWithAnalysisResponse;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.CommentLikeRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebtoonCommentServiceTest {

    @Mock
    private WebtoonCommentRepository commentRepository;
    @Mock
    private CommentLikeRepository likeRepository;
    @Mock
    private WebtoonRepository webtoonRepository;
    @Mock
    private HeaderValidator headerValidator;
    @Mock
    private AsyncAnalysisService asyncAnalysisService;

    @InjectMocks
    private WebtoonCommentService service;

    private AppUser createUser() {
        AppUser user = new AppUser();
        user.setIndexId(1L);
        user.setNickname("닉네임");
        return user;
    }

    private Webtoon createWebtoon() {
        Webtoon webtoon = new Webtoon();
        webtoon.setId(1L);
        return webtoon;
    }

    private WebtoonComment createComment(AppUser user, Webtoon webtoon) {
        return WebtoonComment.builder()
                .id(1L)
                .content("댓글 내용")
                .appUser(user)
                .webtoon(webtoon)
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("댓글 작성 - 성공")
    void addComment() {
        AppUser user = createUser();
        Webtoon webtoon = createWebtoon();
        WebtoonComment comment = createComment(user, webtoon);

        when(headerValidator.getAuthenticatedUser()).thenReturn(user);
        when(webtoonRepository.findById(1L)).thenReturn(Optional.of(webtoon));
        when(commentRepository.save(any())).thenReturn(comment);

        CommentRequestDTO req = new CommentRequestDTO("댓글 내용");
        CommentResponseDTO result = service.addComment(1L, req);

        assertEquals("댓글 내용", result.content());
        verify(asyncAnalysisService).analyzeCommentAsync(any());
    }

    @Test
    @DisplayName("댓글 수정 - 권한 없음 예외")
    void updateComment_권한없음() {
        AppUser user = createUser();
        WebtoonComment comment = createComment(user, createWebtoon());
        AppUser otherUser = new AppUser();
        otherUser.setIndexId(2L);

        when(commentRepository.findByIdAndDeletedDateTimeIsNull(1L)).thenReturn(Optional.of(comment));
        when(headerValidator.getAuthenticatedUser()).thenReturn(otherUser);

        assertThrows(CustomException.class, () -> service.updateComment(1L, "수정"));
    }

    @Test
    @DisplayName("댓글 삭제 - 성공")
    void deleteComment() {
        AppUser user = createUser();
        WebtoonComment comment = createComment(user, createWebtoon());

        when(commentRepository.findByIdAndDeletedDateTimeIsNull(1L)).thenReturn(Optional.of(comment));
        when(headerValidator.getAuthenticatedUser()).thenReturn(user);

        service.deleteComment(1L);

        verify(commentRepository).save(any());
    }

    @Test
    @DisplayName("좋아요 추가 - 성공")
    void addLike() {
        AppUser user = createUser();
        WebtoonComment comment = createComment(user, createWebtoon());

        when(headerValidator.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.findByIdAndDeletedDateTimeIsNull(1L)).thenReturn(Optional.of(comment));
        when(likeRepository.findByAppUserAndWebtoonComment(user, comment)).thenReturn(Optional.empty());

        service.addLike(1L);

        verify(likeRepository).save(any(CommentLike.class));
    }

    @Test
    @DisplayName("좋아요 취소 - 성공")
    void removeLike() {
        AppUser user = createUser();
        WebtoonComment comment = createComment(user, createWebtoon());
        CommentLike like = CommentLike.builder()
                .appUser(user)
                .webtoonComment(comment)
                .likedAt(LocalDateTime.now())
                .isLiked(true)
                .build();

        when(headerValidator.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.findByIdAndDeletedDateTimeIsNull(1L)).thenReturn(Optional.of(comment));
        when(likeRepository.findByAppUserAndWebtoonComment(user, comment)).thenReturn(Optional.of(like));

        service.removeLike(1L);

        verify(likeRepository).save(any(CommentLike.class));
    }

    @Test
    @DisplayName("베스트 댓글 조회 - 성공")
    void getBestComments() {
        // Given
        when(webtoonRepository.existsById(1L)).thenReturn(true);

        // Object[] 배열 생성 (정확한 타입 명시)
        Object[] row = new Object[]{1L, "댓글", "닉네임", 10L};

        // Generic 타입 명시적 지정
        when(commentRepository.findTop3BestCommentsWithLikeCount(eq(1L), any(Pageable.class)))
                .thenReturn(List.<Object[]>of(row));

        // When
        List<CommentResponseDTO> result = service.getBestComments(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals("댓글", result.get(0).content());
        assertEquals(10L, result.get(0).likeCount());
    }
}