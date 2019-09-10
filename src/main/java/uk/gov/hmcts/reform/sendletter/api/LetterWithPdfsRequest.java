package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class LetterWithPdfsRequest {

    private final String type;

    public final List<byte[]> documents;

    @JsonProperty("additional_data")
    private final Map<String, Object> additionalData;

    public LetterWithPdfsRequest(List<byte[]> documents, String type, Map<String, Object> additionalData) {
        this.documents = documents;
        this.type = type;
        this.additionalData = additionalData;
    }

    public String getType() {
        return this.type;
    }

    public Map<String, Object> getAdditionalData() {
        return this.additionalData;
    }

    public List<byte[]> getDocuments() {
        return documents;
    }
}
