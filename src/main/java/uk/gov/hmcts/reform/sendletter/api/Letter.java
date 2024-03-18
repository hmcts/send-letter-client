package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Letter with PDFs request.
 */
public class Letter {

    public final List<Document> documents;

    public final String type;

    @JsonProperty("additional_data")
    public final Map<String, Object> additionalData;

    /**
     * Constructor.
     * @param documents The documents
     * @param type The type
     */
    public Letter(List<Document> documents, String type) {
        this(documents, type, null);
    }

    /**
     * Constructor.
     * @param documents The documents
     * @param type The type
     * @param additionalData The additional data
     */
    public Letter(List<Document> documents, String type, Map<String, Object> additionalData) {
        this.documents = documents;
        this.type = type;
        this.additionalData = additionalData;
    }

    /**
     * Get the type.
     * @return The type
     */
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

    /**
     * Generate a hash code using documents, type, and additional data.
     * @return The hash code integer
     */
    @Override
    public int hashCode() {
        return Objects.hash(documents, type, additionalData);
    }
}
