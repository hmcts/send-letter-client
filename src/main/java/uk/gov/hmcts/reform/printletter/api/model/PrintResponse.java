package uk.gov.hmcts.reform.printletter.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import javax.validation.constraints.NotEmpty;



public class PrintResponse implements Serializable {

    @JsonProperty("print_job")
    @NotEmpty
    public final PrintJob printJob;

    @JsonProperty("upload")
    @NotEmpty
    public final PrintUploadInfo printUploadInfo;

    private PrintResponse() {
        printJob = null;
        printUploadInfo = null;
    }

    public PrintResponse(PrintJob printJob,
                         PrintUploadInfo printUploadInfo) {
        this.printJob = printJob;
        this.printUploadInfo = printUploadInfo;
    }
}
