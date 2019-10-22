package uk.gov.hmcts.reform.sendletter.api.model.v3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Document {

    @JsonProperty("content")
    public final String content;

    @JsonProperty("copies")
    public final int copies;

    public Document(String content, int copies) {
        this.content = content;
        this.copies = copies;
    }
}
