package uk.gov.hmcts.reform.sendletter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.gov.hmcts.reform.printletter.api.PrintLetterApi;
import uk.gov.hmcts.reform.printletter.api.proxy.PrintLetterApiProxy;
import uk.gov.hmcts.reform.sendletter.api.SendLetterApi;
import uk.gov.hmcts.reform.sendletter.api.proxy.SendLetterApiProxy;
import uk.gov.hmcts.reform.sendletter.healthcheck.SendLetterHealthApi;
import uk.gov.hmcts.reform.sendletter.healthcheck.SendLetterHealthIndicator;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = SendLetterAutoConfiguration.class,
    properties = {
        "send-letter.url=localhost"
    }
)
public class AutoConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @DisplayName("Should have HealthApi configured")
    @Test
    public void haveHealthCheck() {
        assertThat(context.getBeanNamesForType(SendLetterHealthApi.class)).hasSize(1);
        assertThat(context.getBeanNamesForType(SendLetterHealthIndicator.class)).hasSize(1);
    }

    @DisplayName("Should have Api configured")
    @Test
    public void haveApi() {
        assertThat(context.getBeanNamesForType(SendLetterApiProxy.class)).hasSize(1);
        assertThat(context.getBeanNamesForType(SendLetterAutoConfiguration.class)).hasSize(1);
        assertThat(context.getBeanNamesForType(RetryTemplate.class)).hasSize(1);
        assertThat(context.getBeanNamesForType(SendLetterApi.class)).hasSize(1);
        assertThat(context.getBeanNamesForType(PrintLetterApi.class)).hasSize(1);
        assertThat(context.getBeanNamesForType(PrintLetterApiProxy.class)).hasSize(1);
    }
}
