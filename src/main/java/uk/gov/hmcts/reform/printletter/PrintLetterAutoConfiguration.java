package uk.gov.hmcts.reform.printletter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.printletter.healthcheck.PrintLetterHealthApi;
import uk.gov.hmcts.reform.printletter.healthcheck.PrintLetterHealthIndicator;

@Configuration
@ConditionalOnProperty(prefix = "send-letter", name = "url")
@EnableFeignClients(basePackages = "uk.gov.hmcts.reform.printletter")
public class PrintLetterAutoConfiguration {

    @Bean
    public PrintLetterHealthIndicator printLetterHealthIndicator(PrintLetterHealthApi printLetterHealthApi) {
        return new PrintLetterHealthIndicator(printLetterHealthApi);
    }
}
