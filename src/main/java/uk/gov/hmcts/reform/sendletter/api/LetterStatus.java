package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

public class LetterStatus {

    public UUID id;

    public String status;

    @JsonProperty("message_id")
    public String messageId;

    @JsonProperty("checksum")
    public String checksum;

    @JsonProperty("created_at")
    public ZonedDateTime createdAt;

    @JsonProperty("sent_to_print_at")
    public ZonedDateTime sentToPrintAt;

    @JsonProperty("printed_at")
    public ZonedDateTime printedAt;

    @JsonProperty("additional_data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Map<String, Object> additionalData;

    public LetterStatus() {

    }

    public LetterStatus(
            final UUID id,
            final String status,
            final String checksum,
            final ZonedDateTime createdAt,
            final ZonedDateTime sentToPrintAt,
            final ZonedDateTime printedAt,
            final Map<String, Object> additionalData
    ) {
        this.id = id;
        this.status = status;
        this.checksum = checksum;
        this.messageId = checksum;
        this.createdAt = createdAt;
        this.sentToPrintAt = sentToPrintAt;
        this.printedAt = printedAt;
        this.additionalData = additionalData;
    }
}