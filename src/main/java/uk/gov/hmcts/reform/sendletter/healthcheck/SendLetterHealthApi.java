package uk.gov.hmcts.reform.sendletter.healthcheck;

import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@FeignClient(name = "send-letter-health", url = "${send-letter.url}",
        configuration = SendLetterHealthApi.SendLetterConfiguration.class)
public interface SendLetterHealthApi {

    @GetMapping(value = "/health", headers = CONTENT_TYPE + "=" + APPLICATION_JSON_UTF8_VALUE)
    InternalHealth health();

    class SendLetterConfiguration {
        @Bean
        @Primary
        Decoder feignDecoder() {
            return new JacksonDecoder();
        }
    }

}
