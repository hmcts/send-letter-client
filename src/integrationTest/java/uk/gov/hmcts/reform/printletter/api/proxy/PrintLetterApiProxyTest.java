package uk.gov.hmcts.reform.printletter.api.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StreamUtils;
import uk.gov.hmcts.reform.printletter.api.model.PrintResponse;
import uk.gov.hmcts.reform.printletter.api.model.v1.Document;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintLetterRequest;
import uk.gov.hmcts.reform.sendletter.SendLetterAutoConfiguration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = SendLetterAutoConfiguration.class,
        properties = {
                "send-letter.url=localhost:6402"
        }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrintLetterApiProxyTest {
    private static WireMockServer wireMockServer;

    @Autowired
    private PrintLetterApiProxy printLetterApiProxy;

    @Autowired
    private ObjectMapper mapper;

    private UUID uuid;

    private PrintResponse printResponse;

    @BeforeAll
    public void spinUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(6402));
        wireMockServer.start();
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        uuid = UUID.randomUUID();
        var json = StreamUtils.copyToString(
                new ClassPathResource("print_job_response.json").getInputStream(), defaultCharset());
        printResponse = mapper.readValue(json, PrintResponse.class);
        var responseJson = mapper.writeValueAsString(printResponse);
        wireMockServer.stubFor(WireMock.put(WireMock.urlEqualTo("/print-jobs/" + uuid))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(responseJson)));
    }

    @AfterAll
    public void shutDown() {
        wireMockServer.stop();
    }

    @Test
    public void testPrintLetter() {
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
        var response = printLetterApiProxy.print(
                "serviceAuthHeader",
                uuid,
                printRequest);
        assert response.printJob != null;
        assertThat(response.printJob.id).isEqualTo(UUID.fromString("33dffc2f-94e0-4584-a973-cc56849ecc0b"));
    }

}
