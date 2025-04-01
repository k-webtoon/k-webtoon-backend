package k_webtoons.k_webtoons.service.webtoonComment;

import jakarta.transaction.Transactional;
import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WebtoonCommentService {

    private final WebtoonCommentRepository commentRepository;
    private final CommentLikeRepository likeRepository;
    private final WebtoonRepository webtoonRepository;
    private final HeaderValidator headerValidator;

    // ========= 공통 검증 메서드 분리 =========
    private void validateCommentOwnership(WebtoonComment comment, AppUser currentUser) {
        if (!comment.getAppUser().equals(currentUser)) {
            throw new CustomException("권한이 없습니다.", "UNAUTHORIZED_ACCESS");
        }
    }

    // ========= 댓글 작성 =========
    public CommentResponseDTO addComment(Long webtoonId, CommentRequestDTO requestDto) {
        try {
            AppUser appUser = headerValidator.getAuthenticatedUser();
            Webtoon webtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new CustomException("웹툰을 찾을 수 없습니다.", "WEBTOON_NOT_FOUND"));

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
                    0L,
                    false
            );
        } catch (Exception e) {
            throw new CustomException("댓글 작성 실패: " + e.getMessage(), "COMMENT_CREATE_FAILED");
        }
    }

    // ========= 댓글 조회 =========
    public Page<CommentResponseDTO> getCommentsByWebtoonId(Long webtoonId, int page, int size) {
        try {
            Webtoon webtoon = webtoonRepository.findById(webtoonId)
                    .orElseThrow(() -> new CustomException("웹툰을 찾을 수 없습니다.", "WEBTOON_NOT_FOUND"));

            // id 역순 정렬
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

            AppUser currentUser = getCurrentUserOrNull();

            Page<WebtoonComment> comments = commentRepository.findByWebtoonAndDeletedDateTimeIsNull(webtoon, pageable);
            return comments.map(comment -> mapCommentToDTO(comment, currentUser));
        } catch (Exception e) {
            throw new CustomException("댓글 조회 실패: " + e.getMessage(), "COMMENT_FETCH_FAILED");
        }
    }

    // ========= 베스트 댓글 조회 =========
    public List<CommentResponseDTO> getBestComments(Long webtoonId) {
        try {
            List<WebtoonComment> bestComments = commentRepository.findTop3BestComments(webtoonId);

            // 현재 사용자 정보 가져오기 (로그인하지 않은 경우 null 반환)
            AppUser currentUser = getCurrentUserOrNull();

            return bestComments.stream()
                    .map(comment -> mapCommentToDTO(comment, currentUser))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException("베스트 댓글 조회 실패: " + e.getMessage(), "BEST_COMMENT_FETCH_FAILED");
        }
    }

    // ========= 댓글 수정 =========
    @Transactional
    public void updateComment(Long id, String newContent) {
        try {
            WebtoonComment comment = commentRepository.findByIdAndDeletedDateTimeIsNull(id)
                    .orElseThrow(() -> new CustomException("댓글을 찾을 수 없습니다.", "COMMENT_NOT_FOUND"));

            AppUser currentUser = headerValidator.getAuthenticatedUser();
            validateCommentOwnership(comment, currentUser);

            comment.setContent(newContent);
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new CustomException("댓글 수정 실패: " + e.getMessage(), "COMMENT_UPDATE_FAILED");
        }
    }

    // ========= 댓글 삭제 =========
    @Transactional
    public void deleteComment(Long id) {
        try {
            WebtoonComment comment = commentRepository.findByIdAndDeletedDateTimeIsNull(id)
                    .orElseThrow(() -> new CustomException("댓글을 찾을 수 없습니다.", "COMMENT_NOT_FOUND"));

            AppUser currentUser = headerValidator.getAuthenticatedUser();
            validateCommentOwnership(comment, currentUser);

            comment.deleteComment();
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new CustomException("댓글 삭제 실패: " + e.getMessage(), "COMMENT_DELETE_FAILED");
        }
    }

    // ========= 좋아요 추가 =========
    @Transactional
    public void addLike(Long commentId) {
        try {
            AppUser appUser = headerValidator.getAuthenticatedUser();
            WebtoonComment comment = commentRepository.findByIdAndDeletedDateTimeIsNull(commentId)
                    .orElseThrow(() -> new CustomException("댓글을 찾을 수 없습니다.", "COMMENT_NOT_FOUND"));

            Optional<CommentLike> existingLike = likeRepository.findByAppUserAndWebtoonComment(appUser, comment);

            if (existingLike.isPresent()) {
                CommentLike like = existingLike.get();
                if (like.isLiked()) {
                    throw new CustomException("이미 좋아요를 눌렀습니다.", "ALREADY_LIKED");
                }
                like.setLiked(true);
                likeRepository.save(like);
            } else {
                CommentLike like = CommentLike.builder()
                        .appUser(appUser)
                        .webtoonComment(comment)
                        .likedAt(LocalDateTime.now())
                        .isLiked(true)
                        .build();
                likeRepository.save(like);
            }
        } catch (Exception e) {
            throw new CustomException("좋아요 추가 실패: " + e.getMessage(), "LIKE_ADD_FAILED");
        }
    }

    // ========= 좋아요 취소 =========
    @Transactional
    public void removeLike(Long commentId) {
        try {
            AppUser appUser = headerValidator.getAuthenticatedUser();
            WebtoonComment comment = commentRepository.findByIdAndDeletedDateTimeIsNull(commentId)
                    .orElseThrow(() -> new CustomException("댓글을 찾을 수 없습니다.", "COMMENT_NOT_FOUND"));

            CommentLike like = likeRepository.findByAppUserAndWebtoonComment(appUser, comment)
                    .orElseThrow(() -> new CustomException("좋아요 기록이 없습니다.", "LIKE_NOT_FOUND"));

            like.setLiked(false);
            likeRepository.save(like);
        } catch (Exception e) {
            throw new CustomException("좋아요 취소 실패: " + e.getMessage(), "LIKE_REMOVE_FAILED");
        }
    }

    // ========= DTO 매핑 메서드 =========
    private CommentResponseDTO mapCommentToDTO(WebtoonComment comment, AppUser currentUser) {
        try {
            // currentUser가 null이면 isLiked는 항상 false
            boolean isLiked = currentUser != null &&
                    likeRepository.findByAppUserAndWebtoonComment(currentUser, comment)
                            .filter(CommentLike::isLiked)
                            .isPresent();

            long likeCount = likeRepository.countByWebtoonCommentAndIsLikedTrue(comment);

            return new CommentResponseDTO(
                    comment.getId(),
                    comment.getContent(),
                    comment.getAppUser().getNickname(),
                    comment.getCreatedDate(),
                    likeCount,
                    isLiked
            );
        } catch (Exception e) {
            throw new CustomException("댓글 매핑 실패: " + e.getMessage(), "COMMENT_MAPPING_FAILED");
        }
    }

    // 사용자 정보 확인
    private AppUser getCurrentUserOrNull() {
        try {
            return headerValidator.getAuthenticatedUser();
        } catch (Exception e) {
            return null; // 인증되지 않은 경우 null 반환
        }
    }

    // ========= 테스트용 메서드 =========
    public CommentResponseDTO getCommentById(Long commentId) {
        try {
            WebtoonComment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new CustomException("댓글을 찾을 수 없습니다.", "COMMENT_NOT_FOUND"));

            return new CommentResponseDTO(
                    comment.getId(),
                    comment.getContent(),
                    comment.getAppUser().getNickname(),
                    comment.getCreatedDate(),
                    (long) comment.getLikes().size(),
                    false
            );
        } catch (Exception e) {
            throw new CustomException("댓글 조회 실패: " + e.getMessage(), "COMMENT_FETCH_FAILED");
        }
    }
}