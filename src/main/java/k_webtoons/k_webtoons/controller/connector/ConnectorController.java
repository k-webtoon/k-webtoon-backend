package k_webtoons.k_webtoons.controller.connector;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.connector.FlaskRequest;
import k_webtoons.k_webtoons.model.connector.FlaskResponse;
import k_webtoons.k_webtoons.security.HeaderValidator;
import k_webtoons.k_webtoons.service.connector.ConnectorService;
import k_webtoons.k_webtoons.service.webtoon.LikeWebtoonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connector")
public class ConnectorController {

    @Autowired
    private ConnectorService connectorService;
    private LikeWebtoonService likeWebtoonService;
    private HeaderValidator headerValidator;

    @PostMapping("/send")
    public FlaskResponse sendMessage (@RequestBody FlaskRequest request) {
        return connectorService.sendToFlask(request);
    }

    @GetMapping("/liked-webtoon-ids")
    public ResponseEntity<Map<String, List<Long>>> getLikedWebtoonIds() {
        // 현재 로그인된 사용자 정보 가져오기
        AppUser currentUser = headerValidator.getAuthenticatedUser();

        // 사용자가 좋아요한 웹툰 ID 리스트 가져오기
        List<Long> likedWebtoonIds = likeWebtoonService.getLikedWebtoonIds(currentUser.getIndexId());

        // JSON 형식으로 반환
        Map<String, List<Long>> response = Map.of("webtoonIds", likedWebtoonIds);
        return ResponseEntity.ok(response);
    }
}
