package uk.gov.hmcts.reform.printletter.api.validation;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;
import uk.gov.hmcts.reform.printletter.api.exception.PrintResponseException;

import java.io.IOException;

import static com.google.common.base.Charsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.gov.hmcts.reform.printletter.api.validation.ValidatePrintResponse.validateResponse;

class ValidatePrintResponseTest {

    @Test
    void print_response_is_valid() throws IOException, PrintResponseException {
        validateResponse(getResource("print_job_response.json"));
    }

    @Test
    void print_response_has_invalid_type_will_throw_exception() throws IOException {
        var json = getResource("print_job_response_type_null.json");
        var printResponseException =
                assertThrows(PrintResponseException.class, () -> validateResponse(json));
        assertThat(printResponseException.getMessage())
                .isEqualTo("print response validation failed #/print_job/type: expected type: String, found: Null");
    }

    @Test
    void print_response_has_invalid_print_job_documents_will_throw_exception() throws IOException {
        var json = getResource("print_job_response_doc_null.json");
        var printResponseException =
                assertThrows(PrintResponseException.class, () -> validateResponse(json));
        assertThat(printResponseException.getMessage())
                .isEqualTo("print response validation failed #/print_job: required key [documents] not found");
    }

    private String getResource(String classPathResource) throws IOException {
        return StreamUtils.copyToString(
                new ClassPathResource(classPathResource).getInputStream(), UTF_8);
    }
}