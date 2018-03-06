package uk.gov.hmcts.reform.sendletter.healthcheck;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import uk.gov.hmcts.reform.sendletter.SendLetterConfiguration;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@FeignClient(name = "send-letter-health", url = "${send-letter.url}",
        configuration = SendLetterConfiguration.class)
public interface SendLetterHealthApi {

    @GetMapping(value = "/health", headers = CONTENT_TYPE + "=" + APPLICATION_JSON_UTF8_VALUE)
    InternalHealth health();
}
