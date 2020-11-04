package uk.gov.hmcts.reform.sendletter.api.config;


import org.springframework.http.HttpStatus;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

public class RetryPolicy extends ExceptionClassifierRetryPolicy {

    public RetryPolicy(int maxAttempts, List<HttpStatus> retryStatuses) {
        final NeverRetryPolicy neverRetryPolicy = new NeverRetryPolicy();
        final SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(maxAttempts);

        this.setExceptionClassifier(classifiable -> {
            if (classifiable instanceof HttpClientErrorException) {
                Optional<HttpStatus> retryStatus = retryStatuses.stream()
                        .filter(value -> value == ((HttpClientErrorException) classifiable).getStatusCode()).findAny();
                if (retryStatus.isPresent()) {
                    return simpleRetryPolicy;
                }
                return neverRetryPolicy;
            }
            return neverRetryPolicy;
        });
    }
}