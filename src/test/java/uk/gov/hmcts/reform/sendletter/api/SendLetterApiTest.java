package uk.gov.hmcts.reform.sendletter.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import uk.gov.hmcts.reform.sendletter.api.config.RetryConfig;
import uk.gov.hmcts.reform.sendletter.api.model.v3.LetterV3;
import uk.gov.hmcts.reform.sendletter.api.proxy.SendLetterApiProxy;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendLetterApiTest {
    private static final String authHeader = "serviceAuthHeader";
    private SendLetterResponse sendLetterResponse;
    private LetterStatus letterStatus;

    @Mock
    private SendLetterApiProxy sendLetterApiProxy;

    private RetryTemplate retryTemplate;

    private SendLetterApi sendLetterApi;

    @BeforeEach
    void setUp() {
        RetryConfig retryConfig = new RetryConfig();
        retryTemplate = retryConfig.retryTemplate();
        sendLetterApi = new SendLetterApi(sendLetterApiProxy, retryTemplate);
        UUID uuid = UUID.randomUUID();
        sendLetterResponse = new SendLetterResponse(uuid);
        letterStatus = new LetterStatus(uuid, "Created", "checksum",
                ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
                ZonedDateTime.now().plusHours(2), Collections.emptyMap());
    }

    @Test
    void testSendTemplateLetterConfirmCreated() {
        Letter letter = new Letter(Collections.emptyList(), "html");
        when(sendLetterApiProxy.sendLetter(eq(authHeader), eq(SendLetterApi.isAsync),eq(letter)))
                .thenReturn(sendLetterResponse);
        when(sendLetterApiProxy.getLetterStatus(eq(sendLetterResponse.letterId.toString()))).thenReturn(letterStatus);
        sendLetterApi.sendLetter(authHeader, letter);
        verify(sendLetterApiProxy).getLetterStatus(eq(sendLetterResponse.letterId.toString()));
    }

    @Test
    void testSendTemplateLetterWithFiveNoLetterFoundException() {
        Letter letter = new Letter(Collections.emptyList(), "html");
        when(sendLetterApiProxy.sendLetter(eq(authHeader), eq(SendLetterApi.isAsync),eq(letter)))
                .thenReturn(sendLetterResponse);
        when(sendLetterApiProxy.getLetterStatus(eq(sendLetterResponse.letterId.toString())))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                    .getReasonPhrase(),null, null, null))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                    .getReasonPhrase(),null, null, null))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                    .getReasonPhrase(),null, null, null))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                    .getReasonPhrase(),null, null, null))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                    .getReasonPhrase(),null, null, null))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                    .getReasonPhrase(),null, null, null))
            .thenReturn(letterStatus);

        sendLetterApi.sendLetter(authHeader, letter);
        verify(sendLetterApiProxy, times(7)).getLetterStatus(eq(sendLetterResponse.letterId.toString()));
    }

    @Test
    void testSendTemplateNoLetterFoundException() {
        Letter letter = new Letter(Collections.emptyList(), "html");
        when(sendLetterApiProxy.sendLetter(eq(authHeader), eq(SendLetterApi.isAsync),eq(letter)))
                .thenReturn(sendLetterResponse);
        when(sendLetterApiProxy.getLetterStatus(eq(sendLetterResponse.letterId.toString())))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null));
        assertThrows(HttpServerErrorException.class, () ->
            sendLetterApi.sendLetter(authHeader, letter));
        verify(sendLetterApiProxy, times(240)).getLetterStatus(eq(sendLetterResponse.letterId.toString()));
    }

    @Test
    void testSendV2LetterConfirmCreated() {
        LetterWithPdfsRequest letter = new LetterWithPdfsRequest(Collections.emptyList(), "pdf",
                Collections.emptyMap());
        when(sendLetterApiProxy.sendLetter(eq(authHeader), eq(SendLetterApi.isAsync),eq(letter)))
                .thenReturn(sendLetterResponse);
        when(sendLetterApiProxy.getLetterStatus(eq(sendLetterResponse.letterId.toString())))
                .thenReturn(letterStatus);
        sendLetterApi.sendLetter(authHeader, letter);
        verify(sendLetterApiProxy).getLetterStatus(eq(sendLetterResponse.letterId.toString()));
    }

    @Test
    void testSendV2LetterWithFiveNoLetterFoundException() {
        LetterWithPdfsRequest letter = new LetterWithPdfsRequest(Collections.emptyList(), "pdf",
                Collections.emptyMap());
        when(sendLetterApiProxy.sendLetter(eq(authHeader), eq(SendLetterApi.isAsync),eq(letter)))
                .thenReturn(sendLetterResponse);
        when(sendLetterApiProxy.getLetterStatus(eq(sendLetterResponse.letterId.toString())))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenReturn(letterStatus);

        sendLetterApi.sendLetter(authHeader, letter);
        verify(sendLetterApiProxy, times(7)).getLetterStatus(eq(sendLetterResponse.letterId.toString()));
    }

    @Test
    void testSendV3LetterConfirmCreated() {
        LetterV3 letter = new LetterV3("pdf", Collections.emptyList(),  Collections.emptyMap());
        when(sendLetterApiProxy.sendLetter(eq(authHeader), eq(SendLetterApi.isAsync),eq(letter)))
                .thenReturn(sendLetterResponse);
        when(sendLetterApiProxy.getLetterStatus(eq(sendLetterResponse.letterId.toString())))
                .thenReturn(letterStatus);
        sendLetterApi.sendLetter(authHeader, letter);
        verify(sendLetterApiProxy).getLetterStatus(eq(sendLetterResponse.letterId.toString()));
    }

    @Test
    void testSendV3LetterWithFiveNoLetterFoundException() {
        LetterV3 letter = new LetterV3("pdf", Collections.emptyList(),  Collections.emptyMap());
        when(sendLetterApiProxy.sendLetter(eq(authHeader), eq(SendLetterApi.isAsync),eq(letter)))
                .thenReturn(sendLetterResponse);
        when(sendLetterApiProxy.getLetterStatus(eq(sendLetterResponse.letterId.toString())))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND
                        .getReasonPhrase(),null, null, null))
                .thenReturn(letterStatus);

        sendLetterApi.sendLetter(authHeader, letter);
        verify(sendLetterApiProxy, times(7))
                .getLetterStatus(eq(sendLetterResponse.letterId.toString()));
    }
}