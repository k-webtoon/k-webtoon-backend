package k_webtoons.k_webtoons.exception;

public record ErrorResponse(
        String message,
        String errorCode
) {}
