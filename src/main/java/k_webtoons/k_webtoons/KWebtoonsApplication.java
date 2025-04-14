package k_webtoons.k_webtoons;

import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@OpenAPIDefinition(
        info = @Info(
                title = "큐레이툰",
                version = "1.0.0",
                description = "웹툰 추천 서비스"
        )
)
@SpringBootApplication
public class KWebtoonsApplication {

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure()
                        .directory("./src/main/resources")
                                .load();

        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });

        SpringApplication.run(KWebtoonsApplication.class, args);
    }
}
