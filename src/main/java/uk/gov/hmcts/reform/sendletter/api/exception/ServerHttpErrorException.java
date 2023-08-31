package uk.gov.hmcts.reform.sendletter.api.exception;

import org.springframework.http.HttpStatus;

public class ServerHttpErrorException extends RuntimeException {
    private final HttpStatus statusCode;
    private final String message;

    public ServerHttpErrorException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
