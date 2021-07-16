package uk.gov.hmcts.reform.printletter.api.model.v1;

import java.io.Serializable;
import java.util.List;

public class PrintRequest implements Serializable {

    private static final long serialVersionUID = 4312487677760800172L;

    public final String type;
    public final List<Document> documents;
    public final String caseId;
    public final String caseRef;
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
