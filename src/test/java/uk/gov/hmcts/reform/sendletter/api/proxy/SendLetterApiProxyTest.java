package uk.gov.hmcts.reform.sendletter.api.proxy;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sendletter.CustomFeignErrorDecoder;

import static org.assertj.core.api.Assertions.assertThat;

class SendLetterApiProxyTest {

    @Test
    public void testConfiguration() {
        SendLetterApiProxy.SendLetterConfiguration configuration = new SendLetterApiProxy.SendLetterConfiguration();
        CustomFeignErrorDecoder customFeignErrorDecoder = configuration.customFeignErrorDecoder();
        assertThat(customFeignErrorDecoder).isNotNull();
    }
}