package k_webtoons.k_webtoons.model.webtoonComment.dto;

import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import lombok.Builder;

import java.util.List;

@Builder
public record CommentWithAnalysisResponse(
        CommentResponseDTO comment,
        List<String> feelTop3,
        String message1,
        String message2,
        String message3
) {
    public static CommentWithAnalysisResponse from(WebtoonComment comment, boolean isLiked) {
        var analysis = comment.getAnalysis();
        return CommentWithAnalysisResponse.builder()
                .comment(CommentResponseDTO.fromEntity(comment, isLiked))
                .feelTop3(analysis != null ? analysis.getFeelTop3() : null)
                .message1(analysis != null ? analysis.getMessage1() : null)
                .message2(analysis != null ? analysis.getMessage2() : null)
                .message3(analysis != null ? analysis.getMessage3() : null)
                .build();
    }
}