package uk.gov.hmcts.reform.sendletter.api.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.sendletter.CustomFeignErrorDecoder;
import uk.gov.hmcts.reform.sendletter.api.Letter;
import uk.gov.hmcts.reform.sendletter.api.LetterStatus;
import uk.gov.hmcts.reform.sendletter.api.LetterWithPdfsRequest;
import uk.gov.hmcts.reform.sendletter.api.SendLetterResponse;
import uk.gov.hmcts.reform.sendletter.api.model.v3.LetterV3;

/**
 * Send letter API proxy.
 */
@FeignClient(name = "send-letter-api", url = "${send-letter.url}",
        configuration = SendLetterApiProxy.SendLetterConfiguration.class)
public interface SendLetterApiProxy {

    /**
     * Send letter.
     * @param serviceAuthHeader The service auth header
     * @param isAsync The is async
     * @param letter The letter
     * @return The send letter response
     */
    @PostMapping(
            path = "/letters",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    SendLetterResponse sendLetter(
            @RequestHeader(name = "ServiceAuthorization", required = false) String serviceAuthHeader,
            @RequestParam (name = "isAsync") String isAsync,
            @RequestBody Letter letter
    );

    /**
     * Send letter.
     * @param serviceAuthHeader The service auth header
     * @param isAsync The is async
     * @param letter The letter
     * @return The send letter response
     */
    @PostMapping(
            path = "/letters",
        consumes = "application/vnd.uk.gov.hmcts.letter-service.in.letter.v2+json",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    SendLetterResponse sendLetter(
        @RequestHeader(name = "ServiceAuthorization", required = false) String serviceAuthHeader,
        @RequestParam (name = "isAsync") String isAsync,
        @RequestBody LetterWithPdfsRequest letter
    );

    /**
     * Send letter.
     * @param serviceAuthHeader The service auth header
     * @param isAsync The is async
     * @param letter The letter
     * @return The send letter response
     */
    @PostMapping(path = "/letters",
        consumes = "application/vnd.uk.gov.hmcts.letter-service.in.letter.v3+json",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    SendLetterResponse sendLetter(
        @RequestHeader(name = "ServiceAuthorization", required = false) String serviceAuthHeader,
        @RequestParam (name = "isAsync") String isAsync,
        @RequestBody LetterV3 letter
    );

    /**
     * Get letter status.
     * @param uuid The uuid
     * @param includeAdditionaInfo The include additiona info
     * @param checkDuplicate The check duplicate
     * @return The letter status
     */
    @GetMapping(path = "/letters/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    LetterStatus getLetterStatus(@PathVariable String uuid,
                                 @RequestParam(name = "include-additional-info") String includeAdditionaInfo,
                                 @RequestParam(name = "check-duplicate") String checkDuplicate);

    /**
     * Send letter configuration.
     */
    class SendLetterConfiguration {
        /**
         * Feign decoder.
         * @param objectMapper The object mapper
         * @return The decoder
         */
        @Bean
        Decoder feignDecoder(ObjectMapper objectMapper) {
            return new JacksonDecoder(objectMapper);
        }

        /**
         * Custom feign error decoder.
         * @return The custom feign error decoder
         */
        @Bean
        public CustomFeignErrorDecoder customFeignErrorDecoder() {
            return new CustomFeignErrorDecoder();
        }
    }
}
