package uk.gov.hmcts.reform.printletter.api.model.v1;

public class Document {

    public final String fileName;
    public final int copies;

    public Document(String fileName, int copies) {
        this.fileName = fileName;
        this.copies = copies;
    }
}
