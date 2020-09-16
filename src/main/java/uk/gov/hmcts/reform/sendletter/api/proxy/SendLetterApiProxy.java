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

@FeignClient(name = "send-letter-api", url = "${send-letter.url}",
        configuration = SendLetterApiProxy.SendLetterConfiguration.class)
public interface SendLetterApiProxy {

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

    @PostMapping(path = "/letters",
        consumes = "application/vnd.uk.gov.hmcts.letter-service.in.letter.v3+json",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    SendLetterResponse sendLetter(
        @RequestHeader(name = "ServiceAuthorization", required = false) String serviceAuthHeader,
        @RequestParam (name = "isAsync") String isAsync,
        @RequestBody LetterV3 letter
    );

    @GetMapping(path = "/letters/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    LetterStatus getLetterStatus(@PathVariable String uuid,
                                 @RequestParam(name = "include-additional-info") String includeAdditionaInfo);

    class SendLetterConfiguration {
        @Bean
        Decoder feignDecoder(ObjectMapper objectMapper) {
            return new JacksonDecoder(objectMapper);
        }

        @Bean
        public CustomFeignErrorDecoder customFeignErrorDecoder() {
            return new CustomFeignErrorDecoder();
        }
    }
}
