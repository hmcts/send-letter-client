package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Letter implements Serializable {

    private static final long serialVersionUID = -7737087336283080072L;

    public final List<Document> documents;

    public final String type;

    @JsonProperty("additional_data")
    public final Map<String, Object> additionalData;

    public Letter(
        @JsonProperty("documents") List<Document> documents,
        @JsonProperty("type") String type,
        @JsonProperty("additional_data") Map<String, Object> additionalData
    ) {
        this.documents = documents;
        this.type = type;
        this.additionalData = additionalData;
    }
}
