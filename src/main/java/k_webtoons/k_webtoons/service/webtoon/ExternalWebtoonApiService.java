package k_webtoons.k_webtoons.service.webtoon;

import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class ExternalWebtoonApiService {

    private final RestTemplate restTemplate;

    public String fetchWebtoonUrl(String titleName) {
        try {
            URI apiUri = UriComponentsBuilder
                    .fromHttpUrl("https://korea-webtoon-api-cc7dda2f0d77.herokuapp.com/webtoons")
                    .queryParam("keyword", titleName)
                    .encode(StandardCharsets.UTF_8)
                    .build()
                    .toUri();

            System.out.println("[DEBUG] 호출 URI: " + apiUri);

            ResponseEntity<String> response = restTemplate.getForEntity(apiUri, String.class);
            String responseBody = response.getBody();
            System.out.println("[DEBUG] API 응답 본문: " + responseBody);

            return parseUrlFromJson(responseBody);
        } catch (Exception e) {
            System.out.println("[ERROR] API 호출 실패: " + e.getMessage());
            return null;
        }
    }

    private String parseUrlFromJson(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray webtoons = json.getJSONArray("webtoons");

            if (webtoons.length() > 0) {
                JSONObject firstWebtoon = webtoons.getJSONObject(0);
                return firstWebtoon.optString("url", null);
            }
        } catch (Exception e) {
            System.out.println("[ERROR] JSON 파싱 실패: " + e.getMessage());
        }
        return null;
    }
}
