package k_webtoons.k_webtoons.service.connector;

import k_webtoons.k_webtoons.model.connector.FlaskRequest;
import k_webtoons.k_webtoons.model.connector.FlaskResponse;
import k_webtoons.k_webtoons.model.connector.WebtoonSimilarity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConnectorService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_URL = "http://localhost:5000/api/process";

    public FlaskResponse sendToFlask(FlaskRequest request) {
        Object response = restTemplate.postForObject(FLASK_URL, request, Object.class);

        if (response instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) response;
            List<Map<String, Object>> result = (List<Map<String, Object>>) map.get("result");
            List<WebtoonSimilarity> similarities = result.stream()
                    .map(item -> new WebtoonSimilarity(
                            Long.parseLong(String.valueOf(item.get("id"))),  // Integer -> Long 변환
                            (String) item.get("title_name"),
                            (Double) item.get("similarity")
                    )).collect(Collectors.toList());
            return new FlaskResponse(similarities);
        } else {
            throw new RuntimeException("Invalid response from Flask server");
        }
    }
}