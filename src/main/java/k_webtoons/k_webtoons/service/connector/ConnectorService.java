package k_webtoons.k_webtoons.service.connector;

import k_webtoons.k_webtoons.model.connector.*;
import k_webtoons.k_webtoons.model.webtoon.Webtoon;
import k_webtoons.k_webtoons.repository.webtoon.WebtoonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConnectorService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_M_URL = "http://localhost:5000/api/process";
    private final String FLASK_C_URL = "http://localhost:5000/api/sendC";
    private final String FLASK_L_URL = "http://localhost:5000/api/sendL";

    @Autowired
    private WebtoonRepository webtoonRepository;

    public ModelMResponse sendToFlask(ModelMRequest request) {
        Object response = restTemplate.postForObject(FLASK_M_URL, request, Object.class);

        if (response instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) response;
            List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("result");
            List<WebtoonSimilarity> similarities = result.stream()
                    .map(item -> new WebtoonSimilarity(
                            Long.parseLong(String.valueOf(item.get("id"))),  // Integer -> Long 변환
                            (String) item.get("title_name"),
                            (Double) item.get("similarity")
                    )).collect(Collectors.toList());
            return new ModelMResponse(similarities);
        } else {
            throw new RuntimeException("Invalid response from Flask server");
        }
    }

    public ModelCResponse processModelC(ModelCRequest request) {
        return restTemplate.postForObject(FLASK_C_URL, request, ModelCResponse.class);
    }


    // sendToFlaskL 메서드
    public List<ModelLResponse> sendToFlaskL(ModelLRequest request) {
        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문(request)과 헤더를 묶음
        HttpEntity<ModelLRequest> entity = new HttpEntity<>(request, headers);

        // Flask 서버로 요청 전송
        ResponseEntity<List<ModelLResponse>> response = restTemplate.exchange(
                FLASK_L_URL,
                HttpMethod.POST,
                entity,  // 여기서 사용!
                new ParameterizedTypeReference<>() {}
        );

        // 응답 처리 로직
        return response.getBody().stream().map(res -> {
            String seenTitle = getWebtoonTitleById(res.seenWebtoonId());
            String recomTitle = getWebtoonTitleById(res.recomWebtoonId());
            String thumbnail = getWebtoonThumbnailUrlById(res.recomWebtoonId());

            return new ModelLResponse(
                    res.seenWebtoonId(),
                    seenTitle,
                    res.recomWebtoonId(),
                    recomTitle,
                    thumbnail,
                    res.cosineSim()
            );
        }).collect(Collectors.toList());
    }

    private String getWebtoonTitleById(Long webtoonId) {
        // WebtoonRepository나 다른 방법을 사용하여 웹툰 ID로 제목을 조회하는 로직
        return webtoonRepository.findTitleById(webtoonId);
    }

    private String getWebtoonThumbnailUrlById(Long webtoonId) {
        // WebtoonRepository나 다른 방법을 사용하여 웹툰 ID로 썸네일 URL을 조회하는 로직
        return webtoonRepository.findThumbnailUrlById(webtoonId);
    }
}