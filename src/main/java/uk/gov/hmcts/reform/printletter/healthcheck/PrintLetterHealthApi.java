package uk.gov.hmcts.reform.printletter.healthcheck;

import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "send-letter-health", url = "${send-letter.url}",
        configuration = PrintLetterHealthApi.PrintLetterHealthConfiguration.class)
public interface PrintLetterHealthApi {

    @GetMapping(value = "/health", headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE)
    InternalHealth health();

    class PrintLetterHealthConfiguration {
        @Bean
        Decoder feignDecoder() {
            return new JacksonDecoder();
        }
    }

}
