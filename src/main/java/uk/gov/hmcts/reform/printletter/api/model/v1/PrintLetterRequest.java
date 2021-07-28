package uk.gov.hmcts.reform.printletter.api.model.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotEmpty;

public class PrintLetterRequest {

    @NotEmpty
    public final List<PrintDocument> documents;

    @NotEmpty
    public final String type;

    @NotEmpty
    @JsonProperty("case_id")
    public final String caseId;

    @NotEmpty
    @JsonProperty("case_ref")
    public final String caseRef;

    @NotEmpty
    @JsonProperty("letter_type")
    public final String letterType;


    private PrintLetterRequest() {
        type = null;
        documents = null;
        caseId = null;
        caseRef = null;
        letterType = null;
    }

    public PrintLetterRequest(
            String type,
            List<PrintDocument> documents,
            String caseId,
            String caseRef,
            String letterType
    ) {
        this.type = type;
        this.documents = documents;
        this.caseId = caseId;
        this.caseRef = caseRef;
        this.letterType = letterType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrintLetterRequest that = (PrintLetterRequest) o;
        return Objects.equals(documents, that.documents) && Objects.equals(type, that.type) && Objects.equals(caseId,
                that.caseId) && Objects.equals(caseRef, that.caseRef) && Objects.equals(letterType, that.letterType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documents, type, caseId, caseRef, letterType);
    }
}
