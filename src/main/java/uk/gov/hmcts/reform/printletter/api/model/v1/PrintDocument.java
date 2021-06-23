package uk.gov.hmcts.reform.printletter.api.model.v1;

import javax.validation.constraints.NotEmpty;

public class PrintDocument {

    @NotEmpty
    public final String fileName;

    @NotEmpty
    public final byte[] content;

    @NotEmpty
    public final int copies;

    public PrintDocument(String fileName, byte[] content, int copies) {
        this.fileName = fileName;
        this.content = content;
        this.copies = copies;
    }
}
