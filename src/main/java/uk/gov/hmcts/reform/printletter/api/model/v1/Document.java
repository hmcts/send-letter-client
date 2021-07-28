package uk.gov.hmcts.reform.printletter.api.model.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class Document {

    @NotEmpty
    @JsonProperty("file_name")
    public final String fileName;

    @NotEmpty
    @JsonProperty("copies_required")
    public final int copies;

    public Document(String fileName, int copies) {
        this.fileName = fileName;
        this.copies = copies;
    }
}
