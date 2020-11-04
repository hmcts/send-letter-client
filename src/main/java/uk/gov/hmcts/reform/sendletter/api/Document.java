package uk.gov.hmcts.reform.sendletter.api;

import java.util.Map;
import java.util.Objects;

public class Document {

    public final String template;

    public final Map<String, Object> values;

    public Document(String template, Map<String, Object> values) {
        this.template = template;
        this.values = values;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Document document = (Document) obj;
        return Objects.equals(template, document.template)
                && Objects.equals(values, document.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(template, values);
    }
}
