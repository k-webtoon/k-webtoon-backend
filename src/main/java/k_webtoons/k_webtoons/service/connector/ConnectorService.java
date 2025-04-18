package k_webtoons.k_webtoons.service.connector;

import k_webtoons.k_webtoons.exception.CustomException;
import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.connector.*;
import k_webtoons.k_webtoons.repository.webtoon.UserWebtoonReviewRepository;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConnectorService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_SEND_M_URL = "http://localhost:5000/api/sendM";  // URL 변경
    private final String FLASK_C_URL = "http://localhost:5000/api/sendC";
    private final String FLASK_L_URL = "http://localhost:5000/api/sendL_if";


    private final WebtoonRepository webtoonRepository;
    private final UserWebtoonReviewRepository userWebtoonReviewRepository;
    private final HeaderValidator headerValidator;

    // sendToFlask() 메서드 전체 개선`
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

    // ModelL 요청 처리
    public List<ModelLResponse> sendToFlaskL(AppUser user, ModelLRequest request) {
        try {
            // 1. 체크박스 상태 매핑 (인기도 → 그림체 → 태그 순서)
            List<Boolean> checkboxState = Arrays.asList(
                    request.usePopularity(),
                    request.useArtStyle(),
                    request.useTags()
            );

            // 2. 사용자 선호 웹툰 ID 조회
            List<Long> webtoonIds = userWebtoonReviewRepository
                    .findUserLikedOrFavoritedWebtoons(user)
                    .stream()
                    .map(review -> review.getWebtoon().getId())
                    .distinct()
                    .collect(Collectors.toList());

            if (webtoonIds.isEmpty()) {
                throw new CustomException("선호하는 웹툰이 없습니다", "NO_PREFERRED_WEBTOONS");
            }

            // 3. Flask 요청 객체 생성
            Map<String, Object> requestMap = Map.of(
                "webtoon_list", webtoonIds,
                "checkbox_state", checkboxState
            );

            // 4. Flask API 호출 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap, headers);

            // 5. 추천 결과 요청 (ResponseEntity 타입 명시적 지정)
            ResponseEntity<List<ModelLResponse>> response = restTemplate.exchange(
                    FLASK_L_URL,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<>() {}  // 타입 추론 개선
            );

            // 6. 응답 검증
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new CustomException("Flask 서버 응답 오류: " + response.getStatusCode(), "FLASK_RESPONSE_ERROR");
            }

            return response.getBody() != null ?
                    response.getBody().stream()
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()) :
                    Collections.emptyList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 헬퍼 메서드들 유지
    private String getWebtoonTitleById(Long webtoonId) {
        return webtoonRepository.findTitleById(webtoonId);
    }

    private String getWebtoonThumbnailUrlById(Long webtoonId) {
        return webtoonRepository.findThumbnailUrlById(webtoonId);
    }
}
