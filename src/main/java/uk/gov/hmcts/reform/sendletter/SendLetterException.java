package uk.gov.hmcts.reform.sendletter;

public class SendLetterException extends RuntimeException {
    public SendLetterException(String message) {
        super(message);
    }
}
