package uk.gov.hmcts.reform.printletter.api;

import com.azure.storage.blob.BlobClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Resources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.printletter.api.exception.PrintResponseException;
import uk.gov.hmcts.reform.printletter.api.model.PrintResponse;
import uk.gov.hmcts.reform.printletter.api.model.v1.Document;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintLetterRequest;
import uk.gov.hmcts.reform.printletter.api.proxy.PrintLetterApiProxy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PrintLetterApiTest {
    private static final String authHeader = "serviceAuthHeader";

    @Mock
    private PrintLetterApiProxy printLetterApiProxy;
    @Mock
    private AzureBlobClient azureBlobClient;
    @Mock
    private BlobClient blobClient;

    private ObjectMapper objectMapper;
    private PrintLetterApi printLetterApi;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        printLetterApi = new PrintLetterApi(printLetterApiProxy, azureBlobClient,objectMapper);
    }

    @Test
    void printLetterCreated() throws IOException, PrintResponseException {
        String json = Resources.toString(getResource("print_job_response.json"), UTF_8);
        PrintResponse printResponse = objectMapper.readValue(json, PrintResponse.class);

        List<Document> documents = List.of(
                new Document(
                        "mypdf.pdf",
                        "sscs-SSC001-mypdf.pdf".getBytes(StandardCharsets.UTF_8),
                        2
                ),
                new Document(
                        "1.pdf",
                        "sscs-SSC001-2.pdf".getBytes(StandardCharsets.UTF_8),
                        1
                )
        );

        PrintLetterRequest printRequest = new PrintLetterRequest(
                "SSC001",
                documents,
                "12345",
                "162MC066",
                "first-contact-pack"
        );


        when(printLetterApiProxy.print(eq(authHeader), any(), eq(printRequest)))
                .thenReturn(printResponse);
        when(azureBlobClient.getBlobClient(any(), any(), any())).thenReturn(blobClient);

        PrintLetterResponse printLetterResponse = printLetterApi.printLetter(authHeader, printRequest);
        assertNotNull(printLetterResponse.letterId);
    }
}