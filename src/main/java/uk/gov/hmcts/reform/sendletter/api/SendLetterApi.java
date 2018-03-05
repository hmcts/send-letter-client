package uk.gov.hmcts.reform.sendletter.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import uk.gov.hmcts.reform.sendletter.SendLetterConfiguration;

@FeignClient(name = "send-letter-api", url = "${send-letter.url}",
        configuration = SendLetterConfiguration.class)
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

}
