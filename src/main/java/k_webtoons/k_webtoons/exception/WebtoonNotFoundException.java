package k_webtoons.k_webtoons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class WebtoonNotFoundException extends RuntimeException {
    public WebtoonNotFoundException(String message) {
        super(message); // 🔥 부모 생성자에 메시지 전달
    }
}
