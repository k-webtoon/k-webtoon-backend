package k_webtoons.k_webtoons.config;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenEntityManagerConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.jpa")
    public JpaProperties jpaProperties() {
        JpaProperties properties = new JpaProperties();
        properties.getProperties().put("spring.jpa.open-in-view", "false");
        return properties;
    }
} 