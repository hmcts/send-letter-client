package uk.gov.hmcts.reform.sendletter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.sendletter.healthcheck.SendLetterHealthApi;
import uk.gov.hmcts.reform.sendletter.healthcheck.SendLetterHealthIndicator;

/**
 * Auto-configuration for send-letter.
 */
@Configuration
@ConditionalOnProperty(prefix = "send-letter", name = "url")
@EnableFeignClients(basePackages = "uk.gov.hmcts.reform.sendletter")
public class SendLetterAutoConfiguration {

    /**
     * Bean for SendLetterHealthIndicator.
     * @param sendLetterHealthApi The SendLetterHealthApi
     * @return The SendLetterHealthIndicator
     */
    @Bean
    public SendLetterHealthIndicator sendLetterHealthIndicator(SendLetterHealthApi sendLetterHealthApi) {
        return new SendLetterHealthIndicator(sendLetterHealthApi);
    }
}
