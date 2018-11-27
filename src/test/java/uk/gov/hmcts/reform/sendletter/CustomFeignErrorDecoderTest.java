package uk.gov.hmcts.reform.sendletter;

import feign.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomFeignErrorDecoderTest {

    private static final CustomFeignErrorDecoder DECODER = new CustomFeignErrorDecoder();

    @DisplayName("Should parse response and return Client specific exception")
    @Test
    public void testClientException() {
        Response response = Response.builder()
                .headers(Collections.emptyMap())
                .status(HttpStatus.NOT_FOUND.value())
                .reason("Could not find")
                .body("some body".getBytes())
                .build();

        assertThat(decode(response))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage(HttpStatus.NOT_FOUND.value() + " Could not find");
    }

    @DisplayName("Should parse response and return Server specific exception")
    @Test
    public void testServerException() {
        Response response = Response.builder()
                .headers(Collections.emptyMap())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .reason("oh no")
                .body("some body".getBytes())
                .build();

        assertThat(decode(response))
                .isInstanceOf(HttpServerErrorException.class)
                .hasMessage(HttpStatus.INTERNAL_SERVER_ERROR.value() + " oh no");
    }

    @DisplayName("Should fail to parse body and throw RuntimeException instead")
    @Test
    public void testEmptyBodyParsing() {
        Response response = Response.builder()
                .headers(Collections.emptyMap())
                .status(HttpStatus.BAD_REQUEST.value())
                .reason("bad")
                .build();

        assertThatCode(() -> decode(response))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to process response body.")
                .hasCauseInstanceOf(NullPointerException.class);
    }

    @DisplayName("Should fail to parse body and throw RuntimeException instead")
    @Test
    public void testFailingBodyParsing() throws IOException {
        Response.Body body = mock(Response.Body.class);
        Response response = Response.builder()
                .headers(Collections.emptyMap())
                .status(HttpStatus.BAD_REQUEST.value())
                .reason("bad")
                .body(body)
                .build();

        when(body.asInputStream()).thenThrow(IOException.class);

        assertThatCode(() -> decode(response))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Failed to process response body.")
                .hasCauseInstanceOf(IOException.class);
    }

    private Exception decode(Response response) {
        return DECODER.decode("methodKey", response);
    }
}
