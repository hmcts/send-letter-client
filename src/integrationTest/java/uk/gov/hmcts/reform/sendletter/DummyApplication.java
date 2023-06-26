package uk.gov.hmcts.reform.sendletter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
    scanBasePackages = {"uk.gov.hmcts.reform.sendletter"}
)
public class DummyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DummyApplication.class, args);
    }
}
