package k_webtoons.k_webtoons.service.webtoonComment;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoonComment.CommentLike;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentRequestDTO;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentResponseDTO;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @InjectMocks
    private WebtoonCommentService commentService;

    @Test
    @DisplayName("댓글 작성 성공 테스트")
    void addCommentSuccessTest() {
        // Given
        Long webtoonId = 1L;
        CommentRequestDTO requestDto = new CommentRequestDTO("이 웹툰 좋아요!");
        
        AppUser mockUser = new AppUser();
        mockUser.setIndexId(1L);
        mockUser.setNickname("테스트유저");
        
        Webtoon mockWebtoon = new Webtoon();
        mockWebtoon.setId(webtoonId);
        mockWebtoon.setTitleName("테스트 웹툰");
        
        WebtoonComment mockComment = new WebtoonComment();
        mockComment.setId(1L);
        mockComment.setContent("이 웹툰 좋아요!");
        mockComment.setAppUser(mockUser);
        mockComment.setWebtoon(mockWebtoon);
        mockComment.setCreatedDate(LocalDateTime.now());
        
        when(headerValidator.getAuthenticatedUser()).thenReturn(mockUser);
        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.of(mockWebtoon));
        when(commentRepository.save(any(WebtoonComment.class))).thenReturn(mockComment);

        // When
        CommentResponseDTO result = commentService.addComment(webtoonId, requestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.content()).isEqualTo("이 웹툰 좋아요!");
        assertThat(result.userNickname()).isEqualTo("테스트유저");
        
        verify(headerValidator, times(1)).getAuthenticatedUser();
        verify(webtoonRepository, times(1)).findById(webtoonId);
        verify(commentRepository, times(1)).save(any(WebtoonComment.class));
    }

    @Test
    @DisplayName("댓글 작성 실패 - 웹툰 없음")
    void addCommentFailureTest() {
        // Given
        Long webtoonId = 999L;
        CommentRequestDTO requestDto = new CommentRequestDTO("이 웹툰 좋아요!");
        
        AppUser mockUser = new AppUser();
        mockUser.setIndexId(1L);
        
        when(headerValidator.getAuthenticatedUser()).thenReturn(mockUser);
        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(CustomException.class, () -> commentService.addComment(webtoonId, requestDto));
        
        verify(headerValidator, times(1)).getAuthenticatedUser();
        verify(webtoonRepository, times(1)).findById(webtoonId);
        verify(commentRepository, never()).save(any(WebtoonComment.class));
    }

    @Test
    @DisplayName("웹툰 댓글 목록 조회 성공 테스트")
    void getCommentsByWebtoonIdSuccessTest() {
        // Given
        Long webtoonId = 1L;
        int page = 0;
        int size = 6;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        
        AppUser user1 = new AppUser();
        user1.setIndexId(1L);
        user1.setNickname("유저1");
        
        AppUser user2 = new AppUser();
        user2.setIndexId(2L);
        user2.setNickname("유저2");
        
        Webtoon webtoon = new Webtoon();
        webtoon.setId(webtoonId);
        
        List<WebtoonComment> commentList = new ArrayList<>();
        
        WebtoonComment comment1 = new WebtoonComment();
        comment1.setId(1L);
        comment1.setContent("첫 번째 댓글");
        comment1.setAppUser(user1);
        comment1.setWebtoon(webtoon);
        comment1.setCreatedDate(LocalDateTime.now());
        comment1.setLikes(new ArrayList<>());
        
        WebtoonComment comment2 = new WebtoonComment();
        comment2.setId(2L);
        comment2.setContent("두 번째 댓글");
        comment2.setAppUser(user2);
        comment2.setWebtoon(webtoon);
        comment2.setCreatedDate(LocalDateTime.now());
        comment2.setLikes(new ArrayList<>());
        
        commentList.add(comment1);
        commentList.add(comment2);
        
        Page<WebtoonComment> commentPage = new PageImpl<>(commentList);
        
        when(webtoonRepository.existsById(webtoonId)).thenReturn(true);
        when(commentRepository.findByWebtoonIdAndDeletedDateTimeIsNull(webtoonId, pageable)).thenReturn(commentPage);
        when(headerValidator.getAuthenticatedUser()).thenReturn(null);

        // When
        Page<CommentResponseDTO> result = commentService.getCommentsByWebtoonId(webtoonId, page, size);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).content()).isEqualTo("첫 번째 댓글");
        assertThat(result.getContent().get(1).content()).isEqualTo("두 번째 댓글");
        
        verify(webtoonRepository, times(1)).existsById(webtoonId);
        verify(commentRepository, times(1)).findByWebtoonIdAndDeletedDateTimeIsNull(eq(webtoonId), any(Pageable.class));
    }

    @Test
    @DisplayName("웹툰 댓글 목록 조회 실패 - 웹툰 없음")
    void getCommentsByWebtoonIdFailureTest() {
        // Given
        Long webtoonId = 999L;
        int page = 0;
        int size = 6;
        
        when(webtoonRepository.existsById(webtoonId)).thenReturn(false);

        // When, Then
        assertThrows(CustomException.class, () -> commentService.getCommentsByWebtoonId(webtoonId, page, size));
        
        verify(webtoonRepository, times(1)).existsById(webtoonId);
        verify(commentRepository, never()).findByWebtoonIdAndDeletedDateTimeIsNull(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("베스트 댓글 조회 테스트")
    void getBestCommentsTest() {
        // Given
        Long webtoonId = 1L;
        
        AppUser user1 = new AppUser();
        user1.setIndexId(1L);
        user1.setNickname("유저1");
        
        AppUser user2 = new AppUser();
        user2.setIndexId(2L);
        user2.setNickname("유저2");
        
        AppUser user3 = new AppUser();
        user3.setIndexId(3L);
        user3.setNickname("유저3");
        
        Webtoon webtoon = new Webtoon();
        webtoon.setId(webtoonId);
        
        List<WebtoonComment> bestComments = new ArrayList<>();
        
        WebtoonComment comment1 = new WebtoonComment();
        comment1.setId(1L);
        comment1.setContent("인기 댓글1");
        comment1.setAppUser(user1);
        comment1.setWebtoon(webtoon);
        comment1.setCreatedDate(LocalDateTime.now());
        
        List<CommentLike> likes1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CommentLike like = new CommentLike();
            like.setLiked(true);
            likes1.add(like);
        }
        comment1.setLikes(likes1);
        
        WebtoonComment comment2 = new WebtoonComment();
        comment2.setId(2L);
        comment2.setContent("인기 댓글2");
        comment2.setAppUser(user2);
        comment2.setWebtoon(webtoon);
        comment2.setCreatedDate(LocalDateTime.now());
        
        List<CommentLike> likes2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            CommentLike like = new CommentLike();
            like.setLiked(true);
            likes2.add(like);
        }
        comment2.setLikes(likes2);
        
        WebtoonComment comment3 = new WebtoonComment();
        comment3.setId(3L);
        comment3.setContent("인기 댓글3");
        comment3.setAppUser(user3);
        comment3.setWebtoon(webtoon);
        comment3.setCreatedDate(LocalDateTime.now());
        
        List<CommentLike> likes3 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            CommentLike like = new CommentLike();
            like.setLiked(true);
            likes3.add(like);
        }
        comment3.setLikes(likes3);
        
        bestComments.add(comment1);
        bestComments.add(comment2);
        bestComments.add(comment3);
        
        when(webtoonRepository.existsById(webtoonId)).thenReturn(true);
        when(commentRepository.findTop3BestComments(webtoonId)).thenReturn(bestComments);
        when(headerValidator.getAuthenticatedUser()).thenReturn(null);

        // When
        List<CommentResponseDTO> result = commentService.getBestComments(webtoonId);

        // Then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).content()).isEqualTo("인기 댓글1");
        assertThat(result.get(1).content()).isEqualTo("인기 댓글2");
        assertThat(result.get(2).content()).isEqualTo("인기 댓글3");
        
        verify(webtoonRepository, times(1)).existsById(webtoonId);
        verify(commentRepository, times(1)).findTop3BestComments(webtoonId);
    }

    @Test
    @DisplayName("댓글 수정 성공 테스트")
    void updateCommentSuccessTest() {
        // Given
        Long commentId = 1L;
        String newContent = "수정된 댓글 내용";
        
        AppUser user = new AppUser();
        user.setIndexId(1L);
        user.setNickname("테스트유저");
        
        WebtoonComment comment = new WebtoonComment();
        comment.setId(commentId);
        comment.setContent("원래 댓글 내용");
        comment.setAppUser(user);
        
        when(commentRepository.findByIdAndDeletedDateTimeIsNull(commentId)).thenReturn(Optional.of(comment));
        when(headerValidator.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.save(any(WebtoonComment.class))).thenReturn(comment);

        // When
        commentService.updateComment(commentId, newContent);

        // Then
        assertThat(comment.getContent()).isEqualTo(newContent);
        
        verify(commentRepository, times(1)).findByIdAndDeletedDateTimeIsNull(commentId);
        verify(headerValidator, times(1)).getAuthenticatedUser();
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("댓글 수정 실패 - 권한 없음")
    void updateCommentFailureTest() {
        // Given
        Long commentId = 1L;
        String newContent = "수정된 댓글 내용";
        
        AppUser commentOwner = new AppUser();
        commentOwner.setIndexId(1L);
        
        AppUser differentUser = new AppUser();
        differentUser.setIndexId(2L);
        
        WebtoonComment comment = new WebtoonComment();
        comment.setId(commentId);
        comment.setContent("원래 댓글 내용");
        comment.setAppUser(commentOwner);
        
        when(commentRepository.findByIdAndDeletedDateTimeIsNull(commentId)).thenReturn(Optional.of(comment));
        when(headerValidator.getAuthenticatedUser()).thenReturn(differentUser);

        // When, Then
        assertThrows(CustomException.class, () -> commentService.updateComment(commentId, newContent));
        
        verify(commentRepository, times(1)).findByIdAndDeletedDateTimeIsNull(commentId);
        verify(headerValidator, times(1)).getAuthenticatedUser();
        verify(commentRepository, never()).save(any(WebtoonComment.class));
    }

    @Test
    @DisplayName("댓글 삭제 성공 테스트")
    void deleteCommentSuccessTest() {
        // Given
        Long commentId = 1L;
        
        AppUser user = new AppUser();
        user.setIndexId(1L);
        
        WebtoonComment comment = new WebtoonComment();
        comment.setId(commentId);
        comment.setAppUser(user);
        
        when(commentRepository.findByIdAndDeletedDateTimeIsNull(commentId)).thenReturn(Optional.of(comment));
        when(headerValidator.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.save(any(WebtoonComment.class))).thenReturn(comment);

        // When
        commentService.deleteComment(commentId);

        // Then
        assertThat(comment.isDeleted()).isTrue();
        
        verify(commentRepository, times(1)).findByIdAndDeletedDateTimeIsNull(commentId);
        verify(headerValidator, times(1)).getAuthenticatedUser();
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("좋아요 추가 성공 테스트")
    void addLikeSuccessTest() {
        // Given
        Long commentId = 1L;
        
        AppUser user = new AppUser();
        user.setIndexId(1L);
        
        WebtoonComment comment = new WebtoonComment();
        comment.setId(commentId);
        
        when(headerValidator.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.findByIdAndDeletedDateTimeIsNull(commentId)).thenReturn(Optional.of(comment));
        when(likeRepository.findByAppUserAndWebtoonComment(user, comment)).thenReturn(Optional.empty());
        when(likeRepository.save(any(CommentLike.class))).thenAnswer(i -> i.getArgument(0));

        // When
        commentService.addLike(commentId);

        // Then
        verify(headerValidator, times(1)).getAuthenticatedUser();
        verify(commentRepository, times(1)).findByIdAndDeletedDateTimeIsNull(commentId);
        verify(likeRepository, times(1)).findByAppUserAndWebtoonComment(user, comment);
        verify(likeRepository, times(1)).save(any(CommentLike.class));
    }

    @Test
    @DisplayName("좋아요 취소 성공 테스트")
    void removeLikeSuccessTest() {
        // Given
        Long commentId = 1L;
        
        AppUser user = new AppUser();
        user.setIndexId(1L);
        
        WebtoonComment comment = new WebtoonComment();
        comment.setId(commentId);
        
        CommentLike like = new CommentLike();
        like.setId(1L);
        like.setAppUser(user);
        like.setWebtoonComment(comment);
        like.setLiked(true);
        
        when(headerValidator.getAuthenticatedUser()).thenReturn(user);
        when(commentRepository.findByIdAndDeletedDateTimeIsNull(commentId)).thenReturn(Optional.of(comment));
        when(likeRepository.findByAppUserAndWebtoonComment(user, comment)).thenReturn(Optional.of(like));
        when(likeRepository.save(any(CommentLike.class))).thenReturn(like);

        // When
        commentService.removeLike(commentId);

        // Then
        assertThat(like.isLiked()).isFalse();
        
        verify(headerValidator, times(1)).getAuthenticatedUser();
        verify(commentRepository, times(1)).findByIdAndDeletedDateTimeIsNull(commentId);
        verify(likeRepository, times(1)).findByAppUserAndWebtoonComment(user, comment);
        verify(likeRepository, times(1)).save(like);
    }
} 