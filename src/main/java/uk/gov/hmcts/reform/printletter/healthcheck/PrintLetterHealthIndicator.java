package uk.gov.hmcts.reform.printletter.healthcheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

public class PrintLetterHealthIndicator implements HealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintLetterHealthIndicator.class);

    private final PrintLetterHealthApi printLetterHealthApi;

    public PrintLetterHealthIndicator(final PrintLetterHealthApi printLetterHealthApi) {
        this.printLetterHealthApi = printLetterHealthApi;
    }

    @Override
    public Health health() {
        try {
            var internalHealth = this.printLetterHealthApi.health();
            return new Health.Builder(internalHealth.getStatus()).build();
        } catch (Exception ex) {
            LOGGER.error("Error on print letter health check", ex);
            return Health.down(ex).build();
        }
    }
}
