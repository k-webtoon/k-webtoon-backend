package k_webtoons.k_webtoons.service.connector;

import k_webtoons.k_webtoons.model.connector.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConnectorService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_URL = "http://localhost:5000/api/process";
    private final String FLASK_C_URL = "http://localhost:5000/api/sendC";

    public ModelMResponse sendToFlask(ModelMRequest request) {
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
            return new ModelMResponse(similarities);
        } else {
            throw new RuntimeException("Invalid response from Flask server");
        }
    }

    public ModelCResponse processModelC(ModelCRequest request) {
        return restTemplate.postForObject(FLASK_C_URL, request, ModelCResponse.class);
    }
}