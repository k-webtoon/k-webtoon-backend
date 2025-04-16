//package k_webtoons.k_webtoons.service.webtoonComment;
//
//import k_webtoons.k_webtoons.exception.CustomException;
//import k_webtoons.k_webtoons.model.auth.AppUser;
//import k_webtoons.k_webtoons.model.webtoon.Webtoon;
//import k_webtoons.k_webtoons.model.webtoonComment.CommentLike;
//import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentRequestDTO;
//import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentResponseDTO;
//import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
//import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
//import k_webtoons.k_webtoons.repository.webtoonComment.CommentLikeRepository;
//import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
//import k_webtoons.k_webtoons.security.HeaderValidator;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class WebtoonCommentServiceTest {
//
//    @Mock
//    private WebtoonCommentRepository commentRepository;
//
//    @Mock
//    private CommentLikeRepository likeRepository;
//
//    @Mock
//    private WebtoonRepository webtoonRepository;
//
//    @Mock
//    private HeaderValidator headerValidator;
//
//    @InjectMocks
//    private WebtoonCommentService commentService;
//
//    @Test
//    @DisplayName("댓글 작성 성공 테스트")
//    void addCommentSuccessTest() {
//        Long webtoonId = 1L;
//        CommentRequestDTO requestDto = new CommentRequestDTO("이 웹툰 좋아요!");
//        AppUser mockUser = new AppUser();
//        mockUser.setIndexId(1L);
//        mockUser.setNickname("테스트유저");
//        Webtoon mockWebtoon = new Webtoon();
//        mockWebtoon.setId(webtoonId);
//        mockWebtoon.setTitleName("테스트 웹툰");
//        WebtoonComment mockComment = new WebtoonComment();
//        mockComment.setId(1L);
//        mockComment.setContent("이 웹툰 좋아요!");
//        mockComment.setAppUser(mockUser);
//        mockComment.setWebtoon(mockWebtoon);
//        mockComment.setCreatedDate(LocalDateTime.now());
//
//        when(headerValidator.getAuthenticatedUser()).thenReturn(mockUser);
//        when(webtoonRepository.findById(webtoonId)).thenReturn(Optional.of(mockWebtoon));
//        when(commentRepository.save(any(WebtoonComment.class))).thenReturn(mockComment);
//
//        CommentResponseDTO result = commentService.addComment(webtoonId, requestDto);
//
//        assertThat(result).isNotNull();
//        assertThat(result.id()).isEqualTo(1L);
//        assertThat(result.content()).isEqualTo("이 웹툰 좋아요!");
//        assertThat(result.nickname()).isEqualTo("테스트유저");
//
//        verify(headerValidator, times(1)).getAuthenticatedUser();
//        verify(webtoonRepository, times(1)).findById(webtoonId);
//        verify(commentRepository, times(1)).save(any(WebtoonComment.class));
//    }
//
//    @Test
//    @DisplayName("베스트 댓글 조회 성공 테스트")
//    void getBestCommentsSuccessTest() {
//        Long webtoonId = 1L;
//        Pageable pageable = PageRequest.of(0, 3);
//
//        List<Object[]> mockResults = new ArrayList<>();
//        mockResults.add(new Object[]{1L, "인기 댓글1", "유저1", 5L});
//        mockResults.add(new Object[]{2L, "인기 댓글2", "유저2", 3L});
//        mockResults.add(new Object[]{3L, "인기 댓글3", "유저3", 2L});
//
//        when(webtoonRepository.existsById(webtoonId)).thenReturn(true);
//        when(commentRepository.findTop3BestCommentsWithLikeCount(eq(webtoonId), eq(pageable)))
//                .thenReturn(mockResults);
//
//        List<CommentResponseDTO> result = commentService.getBestComments(webtoonId);
//
//        assertThat(result).hasSize(3);
//        assertThat(result.get(0).id()).isEqualTo(1L);
//        assertThat(result.get(0).content()).isEqualTo("인기 댓글1");
//        assertThat(result.get(0).nickname()).isEqualTo("유저1");
//        assertThat(result.get(0).likeCount()).isEqualTo(5L);
//
//        verify(webtoonRepository, times(1)).existsById(webtoonId);
//        verify(commentRepository, times(1)).findTop3BestCommentsWithLikeCount(eq(webtoonId), eq(pageable));
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 성공 테스트")
//    void deleteCommentSuccessTest() {
//        Long commentId = 1L;
//
//        AppUser user = new AppUser();
//        user.setIndexId(1L);
//        WebtoonComment comment = new WebtoonComment();
//        comment.setId(commentId);
//        comment.setAppUser(user);
//
//        when(commentRepository.findByIdAndDeletedDateTimeIsNull(commentId))
//                .thenReturn(Optional.of(comment));
//        when(headerValidator.getAuthenticatedUser())
//                .thenReturn(user);
//
//        commentService.deleteComment(commentId);
//
//        assertThat(comment.isDeleted()).isTrue();
//
//        verify(commentRepository, times(1)).findByIdAndDeletedDateTimeIsNull(commentId);
//        verify(headerValidator, times(1)).getAuthenticatedUser();
//        verify(commentRepository, times(1)).save(comment);
//    }
//
//    @Test
//    @DisplayName("좋아요 추가 성공 테스트")
//    void addLikeSuccessTest() {
//        Long commentId = 1L;
//
//        AppUser user = new AppUser();
//        user.setIndexId(1L);
//
//        WebtoonComment comment = new WebtoonComment();
//        comment.setId(commentId);
//
//        when(headerValidator.getAuthenticatedUser()).thenReturn(user);
//        when(commentRepository.findByIdAndDeletedDateTimeIsNull(commentId))
//                .thenReturn(Optional.of(comment));
//        when(likeRepository.findByAppUserAndWebtoonComment(user, comment))
//                .thenReturn(Optional.empty());
//        when(likeRepository.save(any(CommentLike.class))).thenAnswer(i -> i.getArgument(0));
//
//        commentService.addLike(commentId);
//
//        verify(headerValidator, times(1)).getAuthenticatedUser();
//        verify(commentRepository, times(1)).findByIdAndDeletedDateTimeIsNull(commentId);
//        verify(likeRepository, times(1)).findByAppUserAndWebtoonComment(user, comment);
//        verify(likeRepository, times(1)).save(any(CommentLike.class));
//    }
//}