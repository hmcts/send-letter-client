package uk.gov.hmcts.reform.sendletter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import uk.gov.hmcts.reform.sendletter.api.SendLetterApi;
import uk.gov.hmcts.reform.sendletter.healthcheck.SendLetterHealthApi;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "send-letter.url=false"
})
@SpringJUnitConfig
public class NoAutoConfigurationTest {

    @Autowired
    private ApplicationContext context;

    @DisplayName("Should not have HealthApi configured")
    @Test
    public void noHealthCheck() {
        assertThat(context.containsBeanDefinition(SendLetterHealthApi.class.getCanonicalName())).isFalse();
    }

    @DisplayName("Should not have Api configured")
    @Test
    public void noApi() {
        assertThat(context.containsBeanDefinition(SendLetterApi.class.getCanonicalName())).isFalse();
        assertThat(context.containsBeanDefinition(SendLetterAutoConfiguration.class.getCanonicalName())).isFalse();
    }
}
