package k_webtoons.k_webtoons.service.webtoonComment;

import jakarta.transaction.Transactional;
import k_webtoons.k_webtoons.model.user.AppUser;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoonComment.CommentLike;
import k_webtoons.k_webtoons.model.webtoonComment.CommentRequestDTO;
import k_webtoons.k_webtoons.model.webtoonComment.CommentResponseDTO;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.CommentLikeRepository;
import k_webtoons.k_webtoons.repository.webtoonComment.WebtoonCommentRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WebtoonCommentService {

    private final WebtoonCommentRepository commentRepository;
    private final CommentLikeRepository likeRepository;
    private final WebtoonRepository webtoonRepository;
    private final HeaderValidator headerValidator;

    // 댓글 작성
    public CommentResponseDTO addComment(Long webtoonId, CommentRequestDTO requestDto) {
        AppUser appUser = headerValidator.getAuthenticatedUser(); // JWT 헤더 검증 및 사용자 정보 가져오기
        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new RuntimeException("웹툰을 찾을 수 없습니다."));
        WebtoonComment comment = WebtoonComment.builder()
                .appUser(appUser)
                .webtoon(webtoon)
                .content(requestDto.content())
                .build();
        WebtoonComment savedComment = commentRepository.save(comment);
        return new CommentResponseDTO(
                savedComment.getId(),
                savedComment.getContent(),
                appUser.getNickname(),
                savedComment.getCreatedDate(),
                0L // 초기 좋아요 수는 0입니다.
        );
    }

    // 댓글 조회
    public CommentResponseDTO getCommentById(Long id) {
        WebtoonComment comment = commentRepository.findByIdAndDeletedDateTimeIsNull(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        long likeCount = likeRepository.countByWebtoonComment(comment);
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getAppUser().getNickname(),
                comment.getCreatedDate(),
                likeCount
        );
    }

    // 댓글 수정
    @Transactional
    public void updateComment(Long id, String newContent) {
        WebtoonComment comment = commentRepository.findByIdAndDeletedDateTimeIsNull(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        AppUser appUser = headerValidator.getAuthenticatedUser(); // JWT 헤더 검증

        if (!comment.getAppUser().equals(appUser)) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        comment.setContent(newContent);
        commentRepository.save(comment);
    }

    // 댓글 삭제 (소프트 딜리트)
    @Transactional
    public void deleteComment(Long id) {
        WebtoonComment comment = commentRepository.findByIdAndDeletedDateTimeIsNull(id)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        AppUser appUser = headerValidator.getAuthenticatedUser(); // JWT 헤더 검증

        if (!comment.getAppUser().equals(appUser)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        comment.deleteComment();
        commentRepository.save(comment);
    }

    // 좋아요 추가 (중복 방지)
    @Transactional
    public void addLike(Long commentId) {
        AppUser appUser = headerValidator.getAuthenticatedUser(); // JWT 헤더 검증
        WebtoonComment comment = commentRepository.findByIdAndDeletedDateTimeIsNull(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (likeRepository.findByAppUserAndWebtoonComment(appUser, comment).isPresent()) {
            throw new RuntimeException("이미 이 댓글에 좋아요를 눌렀습니다.");
        }

        CommentLike like = CommentLike.builder()
                .appUser(appUser)
                .webtoonComment(comment)
                .likedAt(LocalDateTime.now())
                .build();
        likeRepository.save(like);

        comment.getLikes().add(like);
        commentRepository.save(comment);
    }

    // 좋아요 취소
    @Transactional
    public void removeLike(Long commentId) {
        AppUser appUser = headerValidator.getAuthenticatedUser(); // JWT 헤더 검증
        WebtoonComment comment = commentRepository.findByIdAndDeletedDateTimeIsNull(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        CommentLike like = likeRepository.findByAppUserAndWebtoonComment(appUser, comment)
                .orElseThrow(() -> new RuntimeException("좋아요 기록이 없습니다."));

        likeRepository.delete(like);
    }
}
