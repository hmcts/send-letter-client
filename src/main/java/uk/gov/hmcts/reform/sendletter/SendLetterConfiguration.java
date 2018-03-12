package uk.gov.hmcts.reform.sendletter;

import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

@Configuration
@EnableFeignClients(basePackages = "uk.gov.hmcts.reform.sendletter")
public class SendLetterConfiguration {
    @Bean
    @Primary
    @Scope("prototype")
    Decoder feignDecoder() {
        return new JacksonDecoder();
    }

    @Bean
    public CustomFeignErrorDecoder customFeignErrorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
