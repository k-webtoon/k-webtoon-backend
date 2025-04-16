package k_webtoons.k_webtoons.model.webtoonComment.dto;

import k_webtoons.k_webtoons.model.webtoonComment.CommentLike;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponseDTO(
        Long id,
        String content,
        String nickname,
        java.time.LocalDateTime createdDate,
        Long likeCount,
        boolean isLiked
) {
    public static CommentResponseDTO fromEntity(WebtoonComment comment, boolean isLiked) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getAppUser().getNickname())
                .createdDate(comment.getCreatedDate())
                .likeCount((long) comment.getLikes().size())
                .isLiked(isLiked)
                .build();
    }
}