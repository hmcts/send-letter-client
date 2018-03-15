package uk.gov.hmcts.reform.sendletter;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SendLetterConfiguration {
    @Bean
    @Primary
    Decoder feignDecoder(ObjectMapper objectMapper) {
        return new JacksonDecoder(objectMapper);
    }

    @Bean
    public CustomFeignErrorDecoder customFeignErrorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
