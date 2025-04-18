package k_webtoons.k_webtoons.controller.connector;

import k_webtoons.k_webtoons.model.auth.AppUser;
import k_webtoons.k_webtoons.model.connector.*;
import k_webtoons.k_webtoons.repository.user.UserRepository;
import k_webtoons.k_webtoons.security.HeaderValidator;
import k_webtoons.k_webtoons.service.connector.ConnectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connector")
public class ConnectorController {

    private final ConnectorService connectorService;
    private final HeaderValidator headerValidator;
    private final UserRepository userRepository;

    @PostMapping("/sendM")
    public ModelMResponse sendMessage (@RequestBody ModelMRequest request) {
        return connectorService.sendToFlask(request);
    }

    @PostMapping("/sendC")
    public ModelCResponse sendC(@RequestBody ModelCRequest request) {
        return connectorService.processModelC(request);
    }

    @PostMapping("/sendL_if")
    public ResponseEntity<List<ModelLResponse>> sendL_if(@RequestBody ModelLRequest request) {
        // 인증된 사용자 가져오기
        AppUser user = headerValidator.getAuthenticatedUser();
        System.out.println("인증 사용자: " + user.getUserEmail());

        // 서비스 호출
        List<ModelLResponse> response = connectorService.sendToFlaskL(user, request);

        // 빈 결과 처리 (빈 리스트 반환)
        if (response == null || response.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(response);
    }
}
