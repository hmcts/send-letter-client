package uk.gov.hmcts.reform.printletter.healthcheck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class PrintLetterHealthIndicatorTest {

    @Mock
    private PrintLetterHealthApi healthApi;

    private HealthIndicator indicator;

    @BeforeEach
    void setUp() {
        indicator = new PrintLetterHealthIndicator(healthApi);
    }

    @DisplayName("Should respond back without catching an exception")
    @Test
    void buildNormally() {
        // given
        given(healthApi.health()).willReturn(new InternalHealth(Status.OUT_OF_SERVICE));

        // when
        Health health = indicator.health();

        // then
        assertThat(health.getStatus()).isEqualTo(Status.OUT_OF_SERVICE);
    }

    @DisplayName("Should respond back when exception is thrown")
    @Test
    void buildForException() {
        // given
        given(healthApi.health()).willThrow(new RuntimeException("oh no"));

        // when
        Health health = indicator.health();

        // then
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails())
                .containsKey("error")
                .containsValue(RuntimeException.class.getCanonicalName() + ": oh no");
    }
}