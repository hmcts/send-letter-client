package uk.gov.hmcts.reform.printletter.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.printletter.api.exception.PrintResponseException;
import uk.gov.hmcts.reform.printletter.api.model.Document;
import uk.gov.hmcts.reform.printletter.api.model.PrintResponse;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintDocument;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintLetterRequest;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintRequest;
import uk.gov.hmcts.reform.printletter.api.proxy.PrintLetterApiProxy;
import uk.gov.hmcts.reform.printletter.api.validation.ValidatePrintResponse;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PrintLetterApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintLetterApi.class);

    private final PrintLetterApiProxy printLetterApiProxy;
    private final BlobClientCreator blobClientCreator;
    private final ObjectMapper objectMapper;

    public PrintLetterApi(
            PrintLetterApiProxy printLetterApiProxy,
            BlobClientCreator blobClientCreator,
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
        LOGGER.info("PrintResponse: letter id {}", id);
        var printRequest = getProxyRequest(printLetter);
        PrintResponse response = printLetterApiProxy.print(serviceAuthHeader, id, printRequest);
        try {
            var rep = objectMapper.writeValueAsString(response);
            LOGGER.info("PrintResponse: {}", rep);
            //validate print response
            ValidatePrintResponse.validateResponse(rep);
            //upload pdf files
            uploadPdfFiles(printLetter, response);
            //upload manifest file
            uploadManifestFile(response, rep);
        } catch (Exception e) {
            LOGGER.info("PrintLetterResponse Error occurred", e);
            throw new PrintResponseException("unable to process print request", e);
        }

        return new PrintLetterResponse(id);
    }

    private PrintRequest getProxyRequest(PrintLetterRequest printLetter) {
        List<uk.gov.hmcts.reform.printletter.api.model.v1.Document> listDoc = printLetter.documents.stream()
                .map(pd -> new uk.gov.hmcts.reform.printletter.api.model.v1.Document(pd.fileName, pd.copies))
                .collect(Collectors.toList());

        return new PrintRequest(
                printLetter.type,
                listDoc,
                printLetter.caseId,
                printLetter.caseRef,
                printLetter.letterType);
    }

    private void uploadManifestFile(PrintResponse response, String rep) {
        byte[] data = rep.getBytes(StandardCharsets.UTF_8);
        var blob = response.printUploadInfo.manifestPath;
        var blobClient = blobClientCreator
                .getBlobClient(response, blob);
        blobClient.upload(new ByteArrayInputStream(data), data.length);
        LOGGER.info("finished uploading manifest file {}", blob);
    }

    private void uploadPdfFiles(PrintLetterRequest printLetter, PrintResponse response) {
        PrintDocument reqDoc;
        var documents = response.printJob.documents;
        for (Document document : documents) {
            reqDoc = getContent(printLetter, document.fileName);
            LOGGER.info("uploading blob {}", document.fileName);
            LOGGER.info("uploading reqDoc fileName {}", reqDoc.fileName);
            LOGGER.info("uploading reqDoc copies {}", reqDoc.copies);
            LOGGER.info("uploading reqDoc content length{} in KB", reqDoc.content.length / 1024);
            var blobClient = blobClientCreator
                    .getBlobClient(response, document.uploadToPath);
            blobClient.upload(new ByteArrayInputStream(reqDoc.content), reqDoc.content.length);
            LOGGER.info("uploaded blob {}", reqDoc.fileName);
        }
        LOGGER.info("finished uploading blobs.");
    }

    private PrintDocument getContent(
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
