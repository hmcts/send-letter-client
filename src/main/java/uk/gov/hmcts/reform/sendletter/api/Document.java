package uk.gov.hmcts.reform.sendletter.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Map;

public class Document implements Serializable {

    private static final long serialVersionUID = -2374030102074056382L;

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
