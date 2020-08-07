package uk.gov.hmcts.reform.sendletter.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DocumentTest {

    @DisplayName("Two document contents are the same")
    @Test
    void testTwoDocumentsWithSameContents() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "John");
        Document documentOne = new Document("template_1", values);
        Document documentTwo = new Document("template_1", values);
        assertThat(documentOne).isEqualTo(documentTwo);
    }

    @DisplayName("Document equals to itself")
    @Test
    void testDocumentEqualsToItself() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "John");
        Document documentOne = new Document("template_1", values);
        assertThat(documentOne).isEqualTo(documentOne);
    }

    @DisplayName("Document is not equal to any other object")
    @Test
    void testDocumentDoesNotMatchToOtherObject() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "John");
        Document documentOne = new Document("template_1", values);
        assertThat(documentOne).isNotEqualTo(values);
    }

    @DisplayName("Documents are not equal when templates are different")
    @Test
    void testDocumentsAreNotEqualWhenTemplatesAreDifferent() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "John");
        Document documentOne = new Document("template_1", values);
        Document documentTwo = new Document("template_2", values);
        assertThat(documentOne).isNotEqualTo(documentTwo);
    }

    @DisplayName("Documents are not equal when values are different")
    @Test
    void testDocumentsAreNotEqualWhenValuesAreDifferent() {
        Map<String, Object> valuesOne = new HashMap<>();
        valuesOne.put("name", "John");
        Map<String, Object> valuesTwo = new HashMap<>();
        valuesTwo.put("name", "Laura");
        Document documentOne = new Document("template_1", valuesOne);
        Document documentTwo = new Document("template_1", valuesTwo);
        assertThat(documentOne).isNotEqualTo(documentTwo);
    }

    @DisplayName("Documents with same contains has same hashcode")
    @Test
    void testDocumentsWithSameHashCode() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "John");
        Document documentOne = new Document("template_1", values);
        Document documentTwo = new Document("template_1", values);
        assertThat(documentOne.hashCode()).isEqualTo(documentTwo.hashCode());
    }

    @DisplayName("For documents with different contains hasCode do not match")
    @Test
    void testDocumentsContentsHashCodeDontMatch() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "John");
        Document documentOne = new Document("template_1", values);
        Document documentTwo = new Document("template_2", values);
        assertThat(documentOne.hashCode()).isNotEqualTo(documentTwo.hashCode());

        Map<String, Object> valuesTwo = new HashMap<>();
        valuesTwo.put("name", "Laura");
        Document documentThree = new Document("template_1", valuesTwo);
        assertThat(documentOne.hashCode()).isNotEqualTo(documentThree.hashCode());
    }
}