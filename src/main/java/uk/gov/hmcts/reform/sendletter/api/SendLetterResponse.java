package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class SendLetterResponse {

    @JsonProperty("letter_id")
    public final UUID letterId;

    public SendLetterResponse(UUID letterId) {
        this.letterId = letterId;
    }
}
