package uk.gov.hmcts.reform.sendletter.healthcheck;

import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Send letter health API.
 */
@FeignClient(name = "send-letter-health", url = "${send-letter.url}",
        configuration = SendLetterHealthApi.SendLetterHealthConfiguration.class)
public interface SendLetterHealthApi {

    /**
     * Get the health of the send letter service.
     * @return The health of the send letter service
     */
    @GetMapping(value = "/health", headers = CONTENT_TYPE + "=" + APPLICATION_JSON_VALUE)
    InternalHealth health();

    /**
     * Configuration for the SendLetterHealthApi.
     */
    class SendLetterHealthConfiguration {
        @Bean
        Decoder feignDecoder() {
            return new JacksonDecoder();
        }
    }

}
