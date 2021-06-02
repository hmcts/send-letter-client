package uk.gov.hmcts.reform.printletter.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PrintUploadInfo implements Serializable {

    @JsonProperty("upload_to_container")
    public final String uploadToContainer;

    @JsonProperty("sas")
    public final String sasToken;

    @JsonProperty("manifest_path")
    public final String manifestPath;

    private PrintUploadInfo() {
        uploadToContainer = null;
        sasToken = null;
        manifestPath = null;
    }

    public PrintUploadInfo(String uploadToContainer,
                           String sasToken,
                           String manifestPath) {
        this.uploadToContainer = uploadToContainer;
        this.sasToken = sasToken;
        this.manifestPath = manifestPath;
    }
}
