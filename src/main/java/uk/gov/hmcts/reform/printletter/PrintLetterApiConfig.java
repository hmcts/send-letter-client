package uk.gov.hmcts.reform.printletter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.reform.printletter.api.BlobClientCreator;
import uk.gov.hmcts.reform.printletter.api.PrintLetterApi;
import uk.gov.hmcts.reform.printletter.api.proxy.PrintLetterApiProxy;

@Configuration
@ConditionalOnProperty(prefix = "send-letter", name = "url")
public class PrintLetterApiConfig {

    @Bean
    public PrintLetterApi printLetterApi(
            PrintLetterApiProxy printLetterApiProxy,
            BlobClientCreator blobClientCreator,
            ObjectMapper objectMapper
    ) {
        return new PrintLetterApi(printLetterApiProxy, blobClientCreator, objectMapper);
    }

    @Bean
    public BlobClientCreator blobClientCreator() {
        return new BlobClientCreator();
    }

}
