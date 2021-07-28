package uk.gov.hmcts.reform.printletter.api.model.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class PrintDocument {

    @NotEmpty
    @JsonProperty("file_name")
    public final String fileName;

    @NotEmpty
    public final byte[] content;

    @NotEmpty
    @JsonProperty("copies_required")
    public final int copies;

    public PrintDocument(String fileName, byte[] content, int copies) {
        this.fileName = fileName;
        this.content = content;
        this.copies = copies;
    }
}
