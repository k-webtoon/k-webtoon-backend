package k_webtoons.k_webtoons.service.webtoonComment;

import org.springframework.transaction.annotation.Transactional;
import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.model.webtoonComment.CommentLike;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentRequestDTO;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentResponseDTO;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import k_webtoons.k_webtoons.model.webtoonComment.dto.CommentWithAnalysisResponse;
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


@Service
@RequiredArgsConstructor
public class WebtoonCommentService {

    private final WebtoonCommentRepository commentRepository;
    private final CommentLikeRepository likeRepository;
    private final WebtoonRepository webtoonRepository;
    private final HeaderValidator headerValidator;
    private final AsyncAnalysisService asyncAnalysisService;

    // ========= 공통 검증 메서드 분리 =========
    private void validateCommentOwnership(WebtoonComment comment, AppUser currentUser) {
        if (!comment.getAppUser().equals(currentUser)) {
            throw new CustomException("권한이 없습니다.", "UNAUTHORIZED_ACCESS");
        }
    }

    // ========= 댓글 작성 =========
    @Transactional
    public CommentResponseDTO addComment(Long webtoonId, CommentRequestDTO requestDto) {
        AppUser appUser = headerValidator.getAuthenticatedUser();

        Webtoon webtoon = webtoonRepository.findById(webtoonId)
                .orElseThrow(() -> new CustomException("웹툰 조회 실패", "WEBTOON_NOT_FOUND"));

        // 댓글 생성 시 연관관계 직접 설정
        WebtoonComment comment = WebtoonComment.builder()
                .content(requestDto.content())
                .appUser(appUser)
                .webtoon(webtoon)
                .build();

        WebtoonComment savedComment = commentRepository.save(comment);

        asyncAnalysisService.analyzeCommentAsync(savedComment);

        // 즉시 DTO 변환으로 영속성 컨텍스트 분리
        return convertToDto(savedComment);
    }

    // ========= 웹툰 id로 댓글 조회 =========
    public Page<CommentResponseDTO> getCommentsByWebtoonId(Long webtoonId, int page, int size) {
        // 1. 웹툰 존재 여부 확인 (EXISTS 쿼리)
        boolean exists = webtoonRepository.existsById(webtoonId);
        System.out.println("Webtoon exists? " + exists); // 로깅 추가

        if (!exists) {
            throw new CustomException("웹툰을 찾을 수 없습니다.", "WEBTOON_NOT_FOUND");
        }

        // 2. 댓글 조회
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<WebtoonComment> comments = commentRepository.findByWebtoonIdAndDeletedDateTimeIsNull(webtoonId, pageable);
        System.out.println("Comments found: " + comments.getTotalElements()); // 로깅 추가

        return comments.map(comment -> mapCommentToDTO(comment, getCurrentUserOrNull()));
    }

    // ========= 베스트 댓글 조회 =========
    public List<CommentResponseDTO> getBestComments(Long webtoonId) {
        if (!webtoonRepository.existsById(webtoonId)) {
            throw new CustomException("웹툰을 찾을 수 없습니다.", "WEBTOON_NOT_FOUND");
        }

        Pageable pageable = PageRequest.of(0, 3);
        List<Object[]> bestCommentsWithCount = commentRepository.findTop3BestCommentsWithLikeCount(webtoonId, pageable);

        return bestCommentsWithCount.stream()
                .map(result -> new CommentResponseDTO(
                        ((Number) result[0]).longValue(), // commentId
                        (String) result[1],               // content
                        (String) result[2],               // nickname
                        null,                             // createdDate (생략 가능)
                        ((Number) result[3]).longValue(), // likeCount
                        false                             // isLiked (기본값)
                ))
                .toList();
    }

    private CommentResponseDTO mapCommentToDTOWithLikeCount(WebtoonComment comment, Long likeCount, AppUser currentUser) {
        try {
            String nickname = "알 수 없음";
            if (comment.getAppUser() != null && comment.getAppUser().getNickname() != null) {
                nickname = comment.getAppUser().getNickname();
            }

            boolean isLiked = currentUser != null &&
                    likeRepository.findByAppUserAndWebtoonComment(currentUser, comment)
                            .map(CommentLike::isLiked)
                            .orElse(false);

            return new CommentResponseDTO(
                    comment.getId(),
                    comment.getContent(),
                    nickname,
                    comment.getCreatedDate(),
                    likeCount,
                    isLiked
            );
        } catch (Exception e) {
            throw new CustomException("댓글 매핑 실패: " + e.getMessage(), "COMMENT_MAPPING_FAILED");
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
            // 1. 작성자 닉네임 추출 (null 체크 강화)
            String nickname = "알 수 없음";
            if (comment.getAppUser() != null && comment.getAppUser().getNickname() != null) {
                nickname = comment.getAppUser().getNickname();
            }

            // 2. 좋아요 수 조회 (예외 처리 추가)
            long likeCount = 0L;
            try {
                likeCount = likeRepository.countByWebtoonCommentAndIsLikedTrue(comment);
            } catch (Exception e) {
                System.err.println("좋아요 수 조회 실패: " + e.getMessage());
            }

            // 3. 좋아요 여부 확인
            boolean isLiked = currentUser != null &&
                    likeRepository.findByAppUserAndWebtoonComment(currentUser, comment)
                            .map(CommentLike::isLiked)
                            .orElse(false);

            return new CommentResponseDTO(
                    comment.getId(),
                    comment.getContent(),
                    nickname,
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

    private CommentResponseDTO convertToDto(WebtoonComment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getAppUser().getNickname())
                .createdDate(comment.getCreatedDate())
                .likeCount(likeRepository.countByWebtoonCommentAndIsLikedTrue(comment))
                .isLiked(false) // 초기값 설정
                .build();
    }


    // 모델이 들어간 사용자 댓글 조회
    @Transactional(readOnly = true)
    public Page<CommentWithAnalysisResponse> getCommentsWithAnalysisByWebtoonId(Long webtoonId, int page, int size) {
        boolean exists = webtoonRepository.existsById(webtoonId);
        if (!exists) {
            throw new CustomException("웹툰을 찾을 수 없습니다.", "WEBTOON_NOT_FOUND");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        Page<WebtoonComment> comments = commentRepository.findByWebtoonIdAndDeletedDateTimeIsNull(webtoonId, pageable);

        AppUser currentUser = getCurrentUserOrNull();

        // 트랜잭션 안에서 DTO로 변환
        return comments.map(comment -> {
            boolean isLiked = currentUser != null &&
                    likeRepository.findByAppUserAndWebtoonComment(currentUser, comment)
                            .map(CommentLike::isLiked)
                            .orElse(false);
            // 이 시점에 likes, analysis 등 LAZY 필드 접근 가능
            return CommentWithAnalysisResponse.from(comment, isLiked);
        });
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