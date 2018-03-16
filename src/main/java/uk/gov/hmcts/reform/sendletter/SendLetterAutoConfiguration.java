package uk.gov.hmcts.reform.sendletter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.sendletter.healthcheck.SendLetterHealthApi;
import uk.gov.hmcts.reform.sendletter.healthcheck.SendLetterHealthIndicator;

@Configuration
@ConditionalOnProperty(prefix = "send-letter", name = "url")
@EnableFeignClients(basePackages = "uk.gov.hmcts.reform.sendletter")
public class SendLetterAutoConfiguration {

    @Bean
    public SendLetterHealthIndicator sendLetterHealthIndicator(SendLetterHealthApi sendLetterHealthApi) {
        return new SendLetterHealthIndicator(sendLetterHealthApi);
    }
}
