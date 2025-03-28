package k_webtoons.k_webtoons.controller.connector;

import k_webtoons.k_webtoons.model.connector.FlaskRequest;
import k_webtoons.k_webtoons.model.connector.FlaskResponse;
import k_webtoons.k_webtoons.service.connector.ConnectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connector")
public class ConnectorController {

    @Autowired
    private ConnectorService connectorService;

    @PostMapping("/send")
    public FlaskResponse sendMessage (@RequestBody FlaskRequest request) {
        return connectorService.sendToFlask(request);
    }
}
