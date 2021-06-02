package uk.gov.hmcts.reform.printletter.api;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.printletter.api.model.PrintUploadInfo;

@Component
public class AzureBlobClient {

    public BlobClient getBlobClient(PrintUploadInfo printUploadInfo,
                                     String blobName, String container) {
        return   new BlobClientBuilder()
                .endpoint(printUploadInfo.uploadToContainer)
                .sasToken(printUploadInfo.sasToken)
                .containerName(container)
                .blobName(blobName)
                .buildClient();
    }
}
