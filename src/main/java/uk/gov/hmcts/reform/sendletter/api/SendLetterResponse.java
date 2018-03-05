package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class SendLetterResponse {

    public final UUID letterId;

    public SendLetterResponse(@JsonProperty("letter_id") UUID letterId) {
        this.letterId = letterId;
    }
}
