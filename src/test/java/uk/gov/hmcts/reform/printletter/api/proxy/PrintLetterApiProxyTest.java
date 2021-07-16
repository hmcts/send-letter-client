package uk.gov.hmcts.reform.printletter.api.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sendletter.CustomFeignErrorDecoder;

import static org.assertj.core.api.Assertions.assertThat;

class PrintLetterApiProxyTest {

    @Test
    void testConfiguration() {
        PrintLetterApiProxy.PrintLetterConfiguration configuration = new PrintLetterApiProxy.PrintLetterConfiguration();
        Decoder decoder = configuration.feignDecoder(new ObjectMapper());
        CustomFeignErrorDecoder customFeignErrorDecoder = configuration.customFeignErrorDecoder();
        assertThat(decoder).isNotNull();
        assertThat(customFeignErrorDecoder).isNotNull();
    }
}