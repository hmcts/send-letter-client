package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Letter {

    public final List<Document> documents;

    public final String type;

    @JsonProperty("additional_data")
    public final Map<String, Object> additionalData;

    public Letter(List<Document> documents, String type) {
        this(documents, type, null);
    }

    public Letter(List<Document> documents, String type, Map<String, Object> additionalData) {
        this.documents = documents;
        this.type = type;
        this.additionalData = additionalData;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Letter letter = (Letter) obj;
        return Objects.equals(documents, letter.documents)
                && Objects.equals(type, letter.type)
                && Objects.equals(additionalData, letter.additionalData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documents, type, additionalData);
    }
}
