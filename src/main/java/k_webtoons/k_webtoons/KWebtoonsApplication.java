package k_webtoons.k_webtoons;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
        info = @Info(
                title = "큐레이툰",
                version = "1.0.0",
                description = "웹툰 추천 서비스"
        )
)
@SpringBootApplication
public class KWebtoonsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KWebtoonsApplication.class, args);
    }

}
