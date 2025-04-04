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
    private final String FLASK_SEND_M_URL = "http://localhost:5000/api/sendM";  // URL 변경
    private final String FLASK_C_URL = "http://localhost:5000/api/sendC";
    private final String FLASK_L_URL = "http://localhost:5000/api/sendL";

    @Autowired
    private WebtoonRepository webtoonRepository;

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

    // sendToFlaskL() 메서드 유지
    public List<ModelLResponse> sendToFlaskL(ModelLRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ModelLRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<List<ModelLResponse>> response = restTemplate.exchange(
                FLASK_L_URL,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() {}
        );

        return response.getBody().stream().map(res ->
                new ModelLResponse(
                        res.seenWebtoonId(),
                        getWebtoonTitleById(res.seenWebtoonId()),
                        res.recomWebtoonId(),
                        getWebtoonTitleById(res.recomWebtoonId()),
                        getWebtoonThumbnailUrlById(res.recomWebtoonId()),
                        res.cosineSim()
                )
        ).collect(Collectors.toList());
    }

    // 헬퍼 메서드들 유지
    private String getWebtoonTitleById(Long webtoonId) {
        return webtoonRepository.findTitleById(webtoonId);
    }

    private String getWebtoonThumbnailUrlById(Long webtoonId) {
        return webtoonRepository.findThumbnailUrlById(webtoonId);
    }
}
