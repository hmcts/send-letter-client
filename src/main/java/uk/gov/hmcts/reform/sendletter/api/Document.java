package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Document {

    public final String template;

    public final Map<String, Object> values;

    public Document(
        @JsonProperty("template") String template,
        @JsonProperty("values") Map<String, Object> values
    ) {
        this.template = template;
        this.values = values;
    }
}
