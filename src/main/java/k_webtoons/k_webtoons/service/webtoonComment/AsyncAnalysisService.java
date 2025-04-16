package k_webtoons.k_webtoons.service.webtoonComment;


import k_webtoons.k_webtoons.model.connector.ModelCRequest;
import k_webtoons.k_webtoons.model.connector.ModelCResponse;
import k_webtoons.k_webtoons.model.webtoonComment.CommentAnalysis;
import k_webtoons.k_webtoons.model.webtoonComment.WebtoonComment;
import k_webtoons.k_webtoons.repository.webtoonComment.CommentAnalysisRepository;
import k_webtoons.k_webtoons.service.connector.ConnectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncAnalysisService {

    private final ConnectorService connectorService;
    private final CommentAnalysisRepository commentAnalysisRepository;

    @Async
    public void analyzeCommentAsync(WebtoonComment comment) {
        ModelCRequest request = new ModelCRequest(
                comment.getContent(),
                comment.getWebtoon().getId().intValue()
        );

        try {
            ModelCResponse response = connectorService.processModelC(request);

            commentAnalysisRepository.save(
                    CommentAnalysis.builder()
                            .comment(comment)
                            .feelTop3(response.feelTop3())
                            .message1(response.message1())
                            .message2(response.message2())
                            .message3(response.message3())
                            .build()
            );
        } catch (Exception e) {
            // 재시도 로직 추가 가능
            System.err.println("분석 실패: " + e.getMessage());
        }
    }
}
