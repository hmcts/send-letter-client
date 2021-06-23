package uk.gov.hmcts.reform.printletter.api;

import com.azure.storage.blob.BlobClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import uk.gov.hmcts.reform.printletter.api.exception.PrintResponseException;
import uk.gov.hmcts.reform.printletter.api.model.PrintResponse;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintDocument;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintLetterRequest;
import uk.gov.hmcts.reform.printletter.api.proxy.PrintLetterApiProxy;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
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
    private BlobClientCreator blobClientCreator;
    @Mock
    private BlobClient blobClient;

    private ObjectMapper objectMapper;
    private PrintLetterApi printLetterApi;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        printLetterApi = new PrintLetterApi(printLetterApiProxy, blobClientCreator,objectMapper);
    }

    @Test
    void printLetterCreated() throws IOException, PrintResponseException {
        var json = StreamUtils.copyToString(
                new ClassPathResource("print_job_response.json").getInputStream(), UTF_8);
        var printResponse = objectMapper.readValue(json, PrintResponse.class);
        List<PrintDocument> documents = List.of(
                new PrintDocument(
                        "mypdf.pdf",
                        "sscs-SSC001-mypdf.pdf".getBytes(StandardCharsets.UTF_8),
                        2
                ),
                new PrintDocument(
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
        when(blobClientCreator.getBlobClient(any(), any())).thenReturn(blobClient);

        PrintLetterResponse printLetterResponse = printLetterApi.printLetter(authHeader, printRequest);
        assertNotNull(printLetterResponse.letterId);
    }
}