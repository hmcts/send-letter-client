package uk.gov.hmcts.reform.printletter.api.model.v1;

import java.util.List;
import javax.validation.constraints.NotEmpty;

public class PrintLetterRequest {

    @NotEmpty
    public final List<Document> documents;
    @NotEmpty
    public final String         type;
    @NotEmpty
    public final String         caseId;
    @NotEmpty
    public final String         caseRef;
    @NotEmpty
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
