package uk.gov.hmcts.reform.printletter.api.model.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;

public class PrintRequest implements Serializable {

    private static final long serialVersionUID = 4312487677760800172L;

    @NotEmpty
    public final List<Document> documents;

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


    private PrintRequest() {
        type = null;
        documents = null;
        caseId = null;
        caseRef = null;
        letterType = null;
    }

    public PrintRequest(
        String type,
        List<Document> documents,
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
}
