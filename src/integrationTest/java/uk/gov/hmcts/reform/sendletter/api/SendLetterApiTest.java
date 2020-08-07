package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.sendletter.SendLetterAutoConfiguration;
import uk.gov.hmcts.reform.sendletter.api.model.v3.LetterV3;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = SendLetterAutoConfiguration.class,
        properties = {
                "send-letter.url=localhost:6400"
        }
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SendLetterApiTest {
    private static WireMockServer wireMockServer;

    @Autowired
    private SendLetterApi sendLetterApi;

    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    public void spinUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(6400));
        wireMockServer.start();
    }

    @AfterAll
    public void shutDown() {
        wireMockServer.stop();
    }

    @Test
    public void testSendLetter() throws JsonProcessingException {
        SendLetterResponse sendLetterResponse = new SendLetterResponse(UUID.randomUUID());
        String responseJson = mapper.writeValueAsString(sendLetterResponse);
        wireMockServer.stubFor(WireMock.post(WireMock.urlPathMatching("/letters"))
                .willReturn(WireMock.aResponse().withStatus(200).withBody(responseJson)));
        SendLetterResponse response = sendLetterApi.sendLetter("serviceAuthHeader",
                new Letter(Collections.emptyList(), "test", Collections.emptyMap()));
        assertThat(response.letterId).isEqualTo(sendLetterResponse.letterId);
    }

    @Test
    public void testSendLetter_v2() throws JsonProcessingException {
        SendLetterResponse sendLetterResponse = new SendLetterResponse(UUID.randomUUID());
        String responseJson = mapper.writeValueAsString(sendLetterResponse);
        wireMockServer.stubFor(WireMock.post(WireMock.urlPathMatching("/letters"))
               .willReturn(WireMock.aResponse().withStatus(200).withBody(responseJson)));

        SendLetterResponse response = sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterWithPdfsRequest(Collections.emptyList(), "test", Collections.emptyMap()));
        assertThat(response.letterId).isEqualTo(sendLetterResponse.letterId);
    }

    @Test
    public void testSendLetter_v3() throws JsonProcessingException {
        SendLetterResponse sendLetterResponse = new SendLetterResponse(UUID.randomUUID());
        String responseJson = mapper.writeValueAsString(sendLetterResponse);
        wireMockServer.stubFor(WireMock.post(WireMock.urlPathMatching("/letters"))
               .willReturn(WireMock.aResponse().withStatus(200).withBody(responseJson)));

        SendLetterResponse response = sendLetterApi.sendLetter("serviceAuthHeader",
                new LetterV3("test", Collections.emptyList(), Collections.emptyMap()));
        assertThat(response.letterId).isEqualTo(sendLetterResponse.letterId);
    }
}
