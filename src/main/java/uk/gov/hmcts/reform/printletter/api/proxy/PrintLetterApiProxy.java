package uk.gov.hmcts.reform.printletter.api.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.reform.printletter.api.model.PrintResponse;
import uk.gov.hmcts.reform.printletter.api.model.v1.PrintRequest;
import uk.gov.hmcts.reform.sendletter.CustomFeignErrorDecoder;

import java.util.UUID;

@FeignClient(value = "print-letter-api", url = "${send-letter.url}",
        configuration = PrintLetterApiProxy.PrintLetterConfiguration.class)
public interface PrintLetterApiProxy {

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            path = {"/print-jobs/{id}"}
    )
    PrintResponse print(
            @RequestHeader(name = "ServiceAuthorization", required = false) String serviceAuthHeader,
            @PathVariable("id") UUID id,
            @RequestBody PrintRequest printRequest
    );

    class PrintLetterConfiguration {
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
