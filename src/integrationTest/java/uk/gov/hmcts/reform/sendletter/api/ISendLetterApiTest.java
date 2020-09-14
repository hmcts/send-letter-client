package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpServerErrorException;
import uk.gov.hmcts.reform.sendletter.SendLetterAutoConfiguration;
import uk.gov.hmcts.reform.sendletter.api.model.v3.LetterV3;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = {SendLetterAutoConfiguration.class},
        properties = {
                "send-letter.url=localhost:6401"
        }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ISendLetterApiTest {
    private static WireMockServer wireMockServer;

    @Autowired
    private SendLetterApi sendLetterApi;


    @Autowired
    private ObjectMapper mapper;

    private SendLetterResponse sendLetterResponse;
    private String status;

    @BeforeAll
    public void spinUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(6401));
        wireMockServer.start();
    }

    @BeforeEach
    public void beforeEach() throws JsonProcessingException {
        UUID uuid = UUID.randomUUID();
        sendLetterResponse = new SendLetterResponse(uuid);
        String responseJson = mapper.writeValueAsString(sendLetterResponse);
        wireMockServer.stubFor(WireMock.post(WireMock.urlPathMatching("/letters/yes"))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(responseJson)));

        LetterStatus letterStatus = new LetterStatus(uuid, "Created", "checksum",
                ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
                ZonedDateTime.now().plusHours(2), Collections.emptyMap());
        status = mapper.writeValueAsString(letterStatus);
    }

    @AfterAll
    public void shutDown() {
        wireMockServer.stop();
    }

    @Test
    public void testV1LetterFound() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/letters/" + sendLetterResponse.letterId))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(status)));

        SendLetterResponse sendLetterResponse = sendLetterApi.sendLetter("serviceAuthHeader",
                new Letter(Collections.emptyList(), "test", Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(sendLetterResponse.letterId);
    }

    @Test
    public void testV1LetterNotFound() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/letters/" + sendLetterResponse.letterId))
                .willReturn(WireMock.aResponse().withStatus(404)));

        assertThrows(HttpServerErrorException.class, () -> sendLetterApi.sendLetter("serviceAuthHeader",
                new Letter(Collections.emptyList(), "test", Collections.emptyMap())));
    }

    @Test
    public void testV1LetterFoundInSecondAttempt() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/letters/" + sendLetterResponse.letterId))
                .willReturn(WireMock.aResponse().withStatus(404))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(status)));

        sendLetterApi.sendLetter("serviceAuthHeader",
                new Letter(Collections.emptyList(), "test", Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(sendLetterResponse.letterId);
    }


    @Test
    public void testV2LetterFound() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/letters/" + sendLetterResponse.letterId))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(status)));

        SendLetterResponse sendLetterResponse = sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterWithPdfsRequest(Collections.emptyList(), "test", Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(sendLetterResponse.letterId);
    }

    @Test
    public void testV2LetterNotFound() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/letters/" + sendLetterResponse.letterId))
                .willReturn(WireMock.aResponse().withStatus(404)));

        assertThrows(HttpServerErrorException.class, () -> sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterWithPdfsRequest(Collections.emptyList(), "test", Collections.emptyMap())));
    }

    @Test
    public void testV2LetterFoundInSecondAttempt() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/letters/" + sendLetterResponse.letterId))
                .willReturn(WireMock.aResponse().withStatus(404))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(status)));

        sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterWithPdfsRequest(Collections.emptyList(), "test", Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(sendLetterResponse.letterId);
    }

    @Test
    public void testV3LetterFound() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/letters/" + sendLetterResponse.letterId))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(status)));

        SendLetterResponse sendLetterResponse = sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterV3("test", Collections.emptyList(), Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(sendLetterResponse.letterId);
    }

    @Test
    public void testV3LetterNotFound() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/letters/" + sendLetterResponse.letterId))
                .willReturn(WireMock.aResponse().withStatus(404)));

        assertThrows(HttpServerErrorException.class, () -> sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterV3("test", Collections.emptyList(), Collections.emptyMap())));
    }

    @Test
    public void testV3LetterFoundInSecondAttempt() {
        wireMockServer.stubFor(WireMock.get(WireMock.urlMatching("/letters/" + sendLetterResponse.letterId))
                .willReturn(WireMock.aResponse().withStatus(404))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(status)));

        sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterV3("test", Collections.emptyList(), Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(sendLetterResponse.letterId);
    }
}
