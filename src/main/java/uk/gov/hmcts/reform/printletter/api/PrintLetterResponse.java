package uk.gov.hmcts.reform.printletter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class PrintLetterResponse {

    public final UUID letterId;

    public PrintLetterResponse(@JsonProperty("letter_id") UUID letterId) {
        this.letterId = letterId;
    }
}
