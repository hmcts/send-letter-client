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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import uk.gov.hmcts.reform.sendletter.SendLetterAutoConfiguration;
import uk.gov.hmcts.reform.sendletter.api.model.v3.LetterV3;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = SendLetterAutoConfiguration.class,
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

    private SendLetterResponse expectedSendLetterResponse;
    private String letterStatus;

    @BeforeAll
    public void spinUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(6401));
        wireMockServer.start();
    }

    @BeforeEach
    public void beforeEach() throws JsonProcessingException {
        wireMockServer.resetAll();
        UUID uuid = UUID.randomUUID();
        expectedSendLetterResponse = new SendLetterResponse(uuid);

        String responseJson = mapper.writeValueAsString(expectedSendLetterResponse);
        wireMockServer.stubFor(WireMock.post(urlEqualTo("/letters?isAsync=true"))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(responseJson)));

        LetterStatus letterStatus = new LetterStatus(uuid, "Created", "checksum",
                ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
                ZonedDateTime.now().plusHours(2), Collections.emptyMap(), 1);
        this.letterStatus = mapper.writeValueAsString(letterStatus);
    }

    @AfterAll
    public void shutDown() {
        wireMockServer.stop();
    }

    @Test
    public void testV2LetterFound() {
        stubSingleCallWithStatus(OK);

        SendLetterResponse sendLetterResponse = sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterWithPdfsRequest(Collections.emptyList(), "test", Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(expectedSendLetterResponse.letterId);
        verifyInvocationCount(1);
    }

    @Test
    public void testV2LetterNotFound() {
        stubSingleCallWithStatus(NOT_FOUND);

        assertThrows(HttpServerErrorException.class, () -> sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterWithPdfsRequest(Collections.emptyList(), "test", Collections.emptyMap())));
        verifyInvocationCount(10);
    }

    @Test
    public void testV2LetterFoundInSecondAttempt() {
        stubScenarios();
        SendLetterResponse sendLetterResponse = sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterWithPdfsRequest(Collections.emptyList(), "test", Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(expectedSendLetterResponse.letterId);
        verifyInvocationCount(2);
    }

    @Test
    public void testV3LetterFound() {
        stubSingleCallWithStatus(OK);
        SendLetterResponse sendLetterResponse = sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterV3("test", Collections.emptyList(), Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(expectedSendLetterResponse.letterId);
        verifyInvocationCount(1);
    }

    @Test
    public void testV3LetterNotFound() {
        stubSingleCallWithStatus(NOT_FOUND);

        assertThrows(HttpServerErrorException.class, () -> sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterV3("test", Collections.emptyList(), Collections.emptyMap())));
        verifyInvocationCount(10);
    }

    @Test
    public void testV3LetterFoundInSecondAttempt() {
        stubScenarios();
        SendLetterResponse sendLetterResponse = sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterV3("test", Collections.emptyList(), Collections.emptyMap()));
        assertThat(sendLetterResponse.letterId).isEqualTo(expectedSendLetterResponse.letterId);
        verifyInvocationCount(2);
    }

    @Test
    public void testInternalServerError() {
        stubSingleCallWithStatus(INTERNAL_SERVER_ERROR);
        assertThrows(HttpServerErrorException.class, () -> sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterV3("test", Collections.emptyList(), Collections.emptyMap())));
        verifyInvocationCount(1);
    }

    @Test
    public void testDuplicateLetterRequestError() {
        stubSingleCallWithStatus(CONFLICT);
        assertThrows(HttpClientErrorException.class, () -> sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterV3("test", Collections.emptyList(), Collections.emptyMap())));
        verifyInvocationCount(1);
    }


    private void stubScenarios() {
        wireMockServer.stubFor(get(urlMatching(
                "/letters/" + expectedSendLetterResponse.letterId + "\\" + getRequestParameters()))
                .inScenario("Letter search")
                .whenScenarioStateIs(STARTED).willReturn(WireMock.aResponse().withStatus(404))
                .willSetStateTo("Letter found"));

        wireMockServer.stubFor(get(urlMatching(
                "/letters/" + expectedSendLetterResponse.letterId + "\\" + getRequestParameters()))
                .inScenario("Letter search")
                .whenScenarioStateIs("Letter found")
                .willReturn(WireMock.aResponse().withStatus(200).withBody(letterStatus)));

    }

    private void stubSingleCallWithStatus(HttpStatus status) {
        wireMockServer.stubFor(get(urlMatching(
                "/letters/" + expectedSendLetterResponse.letterId + "\\" + getRequestParameters()))
                .willReturn(WireMock.aResponse().withStatus(status.value()).withBody(letterStatus)));
    }

    private void verifyInvocationCount(int count) {
        wireMockServer.verify(1, postRequestedFor(urlEqualTo(
                "/letters?isAsync=true")));
        wireMockServer.verify(count, getRequestedFor(urlEqualTo(
                "/letters/" + expectedSendLetterResponse.letterId + getRequestParameters())));
    }

    private String getRequestParameters() {
        return "?include-additional-info=false&check-duplicate=true";
    }
}
