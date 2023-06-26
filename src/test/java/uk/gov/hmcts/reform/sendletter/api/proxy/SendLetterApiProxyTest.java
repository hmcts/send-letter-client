package uk.gov.hmcts.reform.sendletter.api.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sendletter.CustomFeignErrorDecoder;

import static org.assertj.core.api.Assertions.assertThat;

class SendLetterApiProxyTest {

    @Test
    public void testConfiguration() {
        SendLetterApiProxy.SendLetterConfiguration configuration = new SendLetterApiProxy.SendLetterConfiguration();
        Decoder decoder = configuration.feignDecoder(new ObjectMapper());
        CustomFeignErrorDecoder customFeignErrorDecoder = configuration.customFeignErrorDecoder();
        assertThat(decoder).isNotNull();
        assertThat(customFeignErrorDecoder).isNotNull();
    }
}