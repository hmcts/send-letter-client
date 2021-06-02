package uk.gov.hmcts.reform.printletter.api.validation;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.printletter.api.exception.PrintResponseException;

import java.io.IOException;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ValidatePrintResponseTest {

    @Test
    void print_response_is_valid() throws IOException, PrintResponseException {
        var json = Resources.toString(getResource("print_job_response.json"), UTF_8);
        ValidatePrintResponse.validateResponse(json);
    }

    @Test
    void print_response_has_invalid_type_will_throw_exception() throws IOException {
        var json = Resources.toString(getResource("print_job_response_type_null.json"), UTF_8);
        var printResponseException = Assertions.assertThrows(PrintResponseException.class, () -> {
            ValidatePrintResponse.validateResponse(json);
        });
        assertThat(printResponseException.getMessage())
                .isEqualTo("print response validation failed #/print_job/type: expected type: String, found: Null");
    }

    @Test
    void print_response_has_invalid_print_job_documents_will_throw_exception() throws IOException {
        var json = Resources.toString(getResource("print_job_response_doc_null.json"), UTF_8);
        var printResponseException = Assertions.assertThrows(PrintResponseException.class, () -> {
            ValidatePrintResponse.validateResponse(json);
        });
        assertThat(printResponseException.getMessage())
                .isEqualTo("print response validation failed #/print_job: required key [documents] not found");
    }
}