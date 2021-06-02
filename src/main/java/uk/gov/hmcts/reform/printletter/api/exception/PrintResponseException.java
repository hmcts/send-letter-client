package uk.gov.hmcts.reform.printletter.api.exception;

public class PrintResponseException extends Exception {
    public PrintResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
