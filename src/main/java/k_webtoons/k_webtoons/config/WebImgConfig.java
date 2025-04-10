package k_webtoons.k_webtoons.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebImgConfig implements WebMvcConfigurer {

    @Value("${upload.image.path}")
    private String uploadImagePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 윈도우 경로 정규화
        String normalizedPath = uploadImagePath
                .replace("\\", "/")
                .replace("//", "/") + "/";

        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///" + normalizedPath)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}