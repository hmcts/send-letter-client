package uk.gov.hmcts.reform.sendletter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
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
        "send-letter.url=false"
    }
)
public class NoAutoConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @DisplayName("Should not have HealthApi configured")
    @Test
    public void noHealthCheck() {
        assertThat(context.getBeanNamesForType(SendLetterHealthApi.class)).hasSize(0);
        assertThat(context.getBeanNamesForType(SendLetterHealthIndicator.class)).hasSize(0);
    }

    @DisplayName("Should not have Api configured")
    @Test
    public void noApi() {
        assertThat(context.getBeanNamesForType(SendLetterApiProxy.class)).hasSize(0);
        assertThat(context.getBeanNamesForType(SendLetterAutoConfiguration.class)).hasSize(0);
        assertThat(context.getBeanNamesForType(RetryTemplate.class)).hasSize(0);
        assertThat(context.getBeanNamesForType(SendLetterApi.class)).hasSize(0);
    }
}
