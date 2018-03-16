package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.reform.sendletter.CustomFeignErrorDecoder;

@FeignClient(name = "send-letter-api", url = "${send-letter.url}",
        configuration = SendLetterApi.SendLetterConfiguration.class)
public interface SendLetterApi {

    @PostMapping(
            path = "/letters",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    SendLetterResponse sendLetter(
            @RequestHeader(name = "ServiceAuthorization", required = false) String serviceAuthHeader,
            @RequestBody Letter letter
    );

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
