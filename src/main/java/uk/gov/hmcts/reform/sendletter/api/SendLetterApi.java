package uk.gov.hmcts.reform.sendletter.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import uk.gov.hmcts.reform.sendletter.api.model.v3.LetterV3;
import uk.gov.hmcts.reform.sendletter.api.proxy.SendLetterApiProxy;

import java.util.Optional;
import java.util.UUID;

@Service
public class SendLetterApi {
    private static final Logger logger = LoggerFactory.getLogger(SendLetterApi.class);

    public static final String isAsync = "true";
    public static final String includeAddtionaInfo = "false";
    public static final String checkDuplicate = "true";

    private final SendLetterApiProxy sendLetterApiProxy;

    private final RetryTemplate retryTemplate;

    public SendLetterApi(SendLetterApiProxy sendLetterApiProxy, RetryTemplate retryTemplate) {
        this.sendLetterApiProxy = sendLetterApiProxy;
        this.retryTemplate = retryTemplate;
    }

    public SendLetterResponse sendLetter(String serviceAuthHeader, Letter letter) {
        SendLetterResponse sendLetterResponse = sendLetterApiProxy.sendLetter(serviceAuthHeader, isAsync, letter);
        confirmRequestIsCreated(sendLetterResponse.letterId);
        return sendLetterResponse;
    }

    public SendLetterResponse sendLetter(String serviceAuthHeader, LetterWithPdfsRequest letter) {
        SendLetterResponse sendLetterResponse = sendLetterApiProxy.sendLetter(serviceAuthHeader, isAsync, letter);
        confirmRequestIsCreated(sendLetterResponse.letterId);
        return sendLetterResponse;

    }

    public SendLetterResponse sendLetter(String serviceAuthHeader, LetterV3 letter) {
        SendLetterResponse sendLetterResponse = sendLetterApiProxy.sendLetter(serviceAuthHeader, isAsync, letter);
        confirmRequestIsCreated(sendLetterResponse.letterId);
        return sendLetterResponse;
    }

    private void confirmRequestIsCreated(UUID letterId) {
        try {
            LetterStatus letterStatus = retryTemplate.execute(arg0 -> {
                logger.info("Retrying for letter id {}", letterId);
                return sendLetterApiProxy.getLetterStatus(letterId.toString(), includeAddtionaInfo, checkDuplicate);
            });
            logger.info("Letter id {} has status {}", letterId, letterStatus.status);
        } catch (HttpClientErrorException httpClientErrorException) {
            logger.error(httpClientErrorException.getMessage(), httpClientErrorException);

            Optional.ofNullable(httpClientErrorException.getMessage())
                .filter(value ->  value.contains("409"))
                .map(value -> {
                    throw  httpClientErrorException;
                });

            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                   HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null, "letter not saved".getBytes(), null);
        }
    }
}
