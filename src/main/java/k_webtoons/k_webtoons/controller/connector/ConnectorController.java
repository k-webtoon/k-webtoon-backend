package k_webtoons.k_webtoons.controller.connector;

import k_webtoons.k_webtoons.model.connector.*;
import k_webtoons.k_webtoons.service.connector.ConnectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/connector")
public class ConnectorController {

    @Autowired
    private ConnectorService connectorService;

    @PostMapping("/sendM")
    public ModelMResponse sendMessage (@RequestBody ModelMRequest request) {
        return connectorService.sendToFlask(request);
    }

    @PostMapping("/sendC")
    public ModelCResponse sendC(@RequestBody ModelCRequest request) {
        return connectorService.processModelC(request);
    }

    @PostMapping("/sendL")
    public List<ModelLResponse> sendL(@RequestBody ModelLRequest request) {
        return connectorService.sendToFlaskL(request);
    }

}
