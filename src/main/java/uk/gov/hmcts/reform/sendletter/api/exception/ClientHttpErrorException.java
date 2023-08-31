package uk.gov.hmcts.reform.sendletter.api.exception;

import org.springframework.http.HttpStatus;

public class ClientHttpErrorException extends RuntimeException {
    private final HttpStatus statusCode;
    private final String message;

    public ClientHttpErrorException(HttpStatus statusCode, String message) {
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
