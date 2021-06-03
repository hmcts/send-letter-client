package uk.gov.hmcts.reform.printletter.api;

import com.azure.storage.blob.BlobClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.google.common.io.Resources;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.printletter.api.exception.PrintResponseException;
import uk.gov.hmcts.reform.printletter.api.model.PrintResponse;
import uk.gov.hmcts.reform.printletter.api.model.v1.Document;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintLetterRequest;
import uk.gov.hmcts.reform.sendletter.SendLetterAutoConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {SendLetterAutoConfiguration.class},
        properties = {
                "send-letter.url=localhost:6405"
        }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IPrintLetterApiTest {
    private static WireMockServer wireMockServer;

    @Autowired
    private PrintLetterApi printLetterApi;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BlobClientCreator blobClientCreator;
    @Mock
    private BlobClient blobClient;

    @BeforeAll
    public void spinUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(6405));
        wireMockServer.start();
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        wireMockServer.resetAll();

        var json = Resources.toString(getResource("print_job_response.json"), UTF_8);
        var printResponse = mapper.readValue(json, PrintResponse.class);
        var printResponseJson = mapper.writeValueAsString(printResponse);

        when(blobClientCreator.getBlobClient(any(), any(), any())).thenReturn(blobClient);

        wireMockServer.stubFor(put(WireMock.anyUrl())
                .willReturn(aResponse().withStatus(200).withBody(printResponseJson)));
    }

    @AfterAll
    public void shutDown() {
        wireMockServer.stop();
    }

    @Test
    public void printLetterFound() throws PrintResponseException {
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

        var printRequest = new PrintLetterRequest(
                "SSC001",
                documents,
                "12345",
                "162MC066",
                "first-contact-pack"
        );


        var response = printLetterApi.printLetter("serviceAuthHeader", printRequest);
        assertNotNull(response.letterId);
        verifyInvocationCount(response.letterId);
    }

    private void verifyInvocationCount(UUID id) {
        wireMockServer.verify(1, putRequestedFor(urlEqualTo(
                "/print-jobs/" + id)));
    }
}
