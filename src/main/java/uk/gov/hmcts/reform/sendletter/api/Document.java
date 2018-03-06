package uk.gov.hmcts.reform.sendletter.api;

import java.util.Map;

public class Document {

    public final String template;

    public final Map<String, Object> values;

    public Document(String template, Map<String, Object> values) {
        this.template = template;
        this.values = values;
    }
}
