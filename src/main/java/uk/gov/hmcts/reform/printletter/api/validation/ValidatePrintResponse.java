package uk.gov.hmcts.reform.printletter.api.validation;

import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.hmcts.reform.printletter.api.PrintLetterApi;
import uk.gov.hmcts.reform.printletter.api.exception.PrintResponseException;


public final class ValidatePrintResponse {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatePrintResponse.class);

    private ValidatePrintResponse() {
        //not called
    }

    public static void validateResponse(String printResponseJson) throws PrintResponseException {
        var jsonSchema = new JSONObject(
                new JSONTokener(PrintLetterApi.class.getResourceAsStream("/print_job_response_schema.json")));
        var jsonObj = new JSONObject(printResponseJson);
        //not validating these. these values get updated by task
        jsonObj.getJSONObject("print_job").remove("printed_at");
        jsonObj.getJSONObject("print_job").remove("sent_to_print_at");

        var schema = SchemaLoader.load(jsonSchema);
        try {
            schema.validate(jsonObj);
        } catch (ValidationException ex) {
            ex.getCausingExceptions().stream()
                    .map(ValidationException::getMessage)
                    .forEach(addWarning -> LOGGER.error("following error detected {}", addWarning));
            throw new PrintResponseException("print response validation failed " + ex.getMessage(), ex);
        }
    }
}
