package uk.gov.hmcts.reform.printletter.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.printletter.api.exception.PrintResponseException;
import uk.gov.hmcts.reform.printletter.api.model.Document;
import uk.gov.hmcts.reform.printletter.api.model.PrintResponse;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintLetterRequest;
import uk.gov.hmcts.reform.printletter.api.proxy.PrintLetterApiProxy;
import uk.gov.hmcts.reform.printletter.api.validation.ValidatePrintResponse;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PrintLetterApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintLetterApi.class);

    private final PrintLetterApiProxy printLetterApiProxy;
    private final BlobClientCreator blobClientCreator;
    private final ObjectMapper objectMapper;

    public PrintLetterApi(PrintLetterApiProxy printLetterApiProxy, BlobClientCreator blobClientCreator,
                          ObjectMapper objectMapper) {
        this.printLetterApiProxy = printLetterApiProxy;
        this.blobClientCreator = blobClientCreator;
        this.objectMapper = objectMapper;
    }

    public PrintLetterResponse printLetter(
            String serviceAuthHeader,
            PrintLetterRequest printLetter
    ) throws PrintResponseException {

        var id = UUID.randomUUID();
        PrintResponse response = printLetterApiProxy.print(serviceAuthHeader, id, printLetter);

        try {

            var rep = objectMapper.writeValueAsString(response);

            //validate print response
            ValidatePrintResponse.validateResponse(rep);

            //upload pdf files
            uploadPdfFiles(printLetter, response);

            //upload manifest file
            uploadManifestFile(response, rep);
        } catch (Exception e) {
            throw new PrintResponseException("unable to process print request", e);
        }

        return new PrintLetterResponse(id);
    }

    private void uploadManifestFile(PrintResponse response, String rep) {
        byte[] data = rep.getBytes(StandardCharsets.UTF_8);
        var blobClient = blobClientCreator.getBlobClient(
                response.printUploadInfo,
                response.printUploadInfo.manifestPath,
                response.printJob.containerName
        );
        blobClient.upload(new ByteArrayInputStream(data), data.length);
        LOGGER.info("finished uploading manifest file.");
    }

    private void uploadPdfFiles(PrintLetterRequest printLetter, PrintResponse response) {
        uk.gov.hmcts.reform.printletter.api.model.v1.Document reqDoc;
        var documents = response.printJob.documents;
        var container = response.printJob.containerName;
        for (Document document : documents) {
            reqDoc = getContent(printLetter, document.fileName);
            LOGGER.info("uploading files {}", document.fileName);
            var blobClient = blobClientCreator
                    .getBlobClient(response.printUploadInfo, document.uploadToPath, container);
            blobClient.upload(new ByteArrayInputStream(reqDoc.content), reqDoc.content.length);
        }
        LOGGER.info("finished uploading files.");
    }

    private uk.gov.hmcts.reform.printletter.api.model.v1.Document getContent(
            PrintLetterRequest printLetter,
            String filename
    ) {
        return Optional.ofNullable(printLetter.documents)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .filter(p -> p.fileName.equals(filename))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("unable to find file content"));
    }
}
