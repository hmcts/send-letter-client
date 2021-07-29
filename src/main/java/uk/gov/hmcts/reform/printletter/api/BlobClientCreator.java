package uk.gov.hmcts.reform.printletter.api;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.printletter.api.model.PrintResponse;

@Component
public class BlobClientCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlobClientCreator.class);

    public BlobClient getBlobClient(
            final PrintResponse response,
            final String blobName
    ) {
        var uploadToContainer = response.printUploadInfo.uploadToContainer;
        var sasToken = response.printUploadInfo.sasToken;
        var containerName = response.printJob.containerName;

        LOGGER.info("BlobClientCreator::uploadToContainer: {}", uploadToContainer);
        LOGGER.info("BlobClientCreator::sasToken: {}", sasToken);
        LOGGER.info("BlobClientCreator::containerName: {}", containerName);
        LOGGER.info("BlobClientCreator::blobName: {}", blobName);

        return new BlobClientBuilder()
                .endpoint(uploadToContainer)
                .sasToken(sasToken)
                .containerName(containerName)
                .blobName(blobName)
                .buildClient();
    }
}
