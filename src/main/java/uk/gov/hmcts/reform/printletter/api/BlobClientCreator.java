package uk.gov.hmcts.reform.printletter.api;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.printletter.api.model.PrintResponse;

@Component
public class BlobClientCreator {

    public BlobClient getBlobClient(
            final PrintResponse response,
            final String blobName
    ) {
        return new BlobClientBuilder()
                .endpoint(response.printUploadInfo.uploadToContainer)
                .sasToken(response.printUploadInfo.sasToken)
                .containerName(response.printJob.containerName)
                .blobName(blobName)
                .buildClient();
    }
}
