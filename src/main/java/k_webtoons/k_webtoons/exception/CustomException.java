package k_webtoons.k_webtoons.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String errorCode;


    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
