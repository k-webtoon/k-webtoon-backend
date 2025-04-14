package k_webtoons.k_webtoons.service.connector;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.connector.*;
import k_webtoons.k_webtoons.model.webtoon.UserWebtoonReview;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.repository.webtoon.UserWebtoonReviewRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConnectorService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_SEND_M_URL = "http://localhost:5000/api/sendM";  // URL 변경
    private final String FLASK_C_URL = "http://localhost:5000/api/sendC";
    private final String FLASK_L_URL = "http://localhost:5000/api/sendL_if";


    private WebtoonRepository webtoonRepository;
    private UserWebtoonReviewRepository userWebtoonReviewRepository;
    private HeaderValidator headerValidator;

    // sendToFlask() 메서드 전체 개선
    public ModelMResponse sendToFlask(ModelMRequest request) {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 엔티티 생성
        HttpEntity<ModelMRequest> entity = new HttpEntity<>(request, headers);

        // Flask 서버로 요청 전송 (exchange 방식으로 변경)
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                FLASK_SEND_M_URL,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        // 응답 처리
        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || !responseBody.containsKey("result")) {
            throw new RuntimeException("Flask 서버 응답 오류");
        }

        List<Map<String, Object>> result = (List<Map<String, Object>>) responseBody.get("result");
        return new ModelMResponse(
                result.stream()
                        .map(item -> new WebtoonSimilarity(
                                Long.parseLong(String.valueOf(item.get("id"))),
                                (String) item.get("title_name"),
                                ((Number) item.get("similarity")).doubleValue()
                        ))
                        .collect(Collectors.toList())
        );
    }

    // processModelC() 메서드 유지
    public ModelCResponse processModelC(ModelCRequest request) {
        return restTemplate.postForObject(
                FLASK_C_URL,
                request,
                ModelCResponse.class
        );
    }

    // ModelL 요청 처리 (추가된 메서드)
    public List<ModelLResponse> sendToFlaskL(AppUser user, ModelLRequest request) {
        // 1. 사용자의 좋아요/즐겨찾기 웹툰 조회
        List<UserWebtoonReview> reviews = userWebtoonReviewRepository.findUserLikedOrFavoritedWebtoons(user);

        // 2. 웹툰 ID 추출 (중복 제거)
        List<Long> webtoonIds = reviews.stream()
                .map(review -> review.getWebtoon().getId())
                .distinct()
                .collect(Collectors.toList());

        // 3. Flask로 전송할 요청 생성
        ModelLRequest flaskRequest = new ModelLRequest(webtoonIds, request.checkboxState());

        // 4. Flask API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ModelLRequest> entity = new HttpEntity<>(flaskRequest, headers);

        ResponseEntity<List<ModelLResponse>> response = restTemplate.exchange(
                FLASK_L_URL,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<List<ModelLResponse>>() {}
        );

        return response.getBody();
    }


    // 헬퍼 메서드들 유지
    private String getWebtoonTitleById(Long webtoonId) {
        return webtoonRepository.findTitleById(webtoonId);
    }

    private String getWebtoonThumbnailUrlById(Long webtoonId) {
        return webtoonRepository.findThumbnailUrlById(webtoonId);
    }
}
