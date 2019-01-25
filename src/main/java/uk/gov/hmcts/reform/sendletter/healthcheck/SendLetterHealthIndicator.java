package uk.gov.hmcts.reform.sendletter.healthcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class SendLetterHealthIndicator implements HealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendLetterHealthIndicator.class);

    private final SendLetterHealthApi sendLetterHealthApi;

    public SendLetterHealthIndicator(final SendLetterHealthApi sendLetterHealthApi) {
        this.sendLetterHealthApi = sendLetterHealthApi;
    }

    @Override
    public Health health() {
        try {
            InternalHealth internalHealth = this.sendLetterHealthApi.health();
            return new Health.Builder(internalHealth.getStatus()).build();
        } catch (Exception ex) {
            LOGGER.error("Error on send letter healthcheck", ex);
            return Health.down(ex).build();
        }
    }
}
