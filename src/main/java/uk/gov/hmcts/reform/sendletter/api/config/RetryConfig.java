package uk.gov.hmcts.reform.sendletter.api.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import uk.gov.hmcts.reform.sendletter.api.SendLetterApi;
import uk.gov.hmcts.reform.sendletter.api.proxy.SendLetterApiProxy;

import java.util.Collections;

@Configuration
@ConditionalOnProperty(prefix = "send-letter", name = "url")
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setThrowLastExceptionOnExhausted(false);

        ExponentialBackOffPolicy exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setMaxInterval(500);
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        retryTemplate.setRetryPolicy(new RetryPolicy(240, Collections.singletonList(HttpStatus.NOT_FOUND)));

        return retryTemplate;
    }

    @Bean
    public SendLetterApi sendLetterApi(SendLetterApiProxy sendLetterApiProxy, RetryTemplate retryTemplate) {
        return new SendLetterApi(sendLetterApiProxy, retryTemplate);
    }
}
