package uk.gov.hmcts.reform.sendletter.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Letter tests")
class LetterTest {

    @DisplayName("Two Letters are equal")
    @Test
    void testTwoLetterAreEquals() {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put("additionalData", "value");
        Letter letterOne = new Letter(getDocuments(), "test", additionalData);
        Letter letterTwo = new Letter(getDocuments(), "test", additionalData);
        assertThat(letterOne).isEqualTo(letterTwo);
    }

    @DisplayName("Same Letter object are equals")
    @Test
    void testSameLetterAreEquals() {
        Letter letter = new Letter(getDocuments(), "test");
        assertThat(letter).isEqualTo(letter);
    }

    @DisplayName("Letter and document objects are not same")
    @Test
    void testLetterDoesNotMatchToDocument() {
        List<Document> documents = getDocuments();
        Letter letter = new Letter(documents, "test");
        assertThat(letter).isNotEqualTo(documents.get(0));
        assertThat(letter).isNotEqualTo(documents);
    }

    @DisplayName("Letter is not equal null")
    @Test
    void testLetterDoesNotMatchNullObject() {
        List<Document> documents = getDocuments();
        Letter letter = new Letter(documents, "test");
        assertThat(letter).isNotEqualTo(null);
    }

    @DisplayName("Letter with non matching types are not same")
    @Test
    void testLetterTypesAreNotSame() {
        Letter letterOne = new Letter(getDocuments(), "test");
        Letter letterTwo = new Letter(getDocuments(), "not matching");
        assertThat(letterOne).isNotEqualTo(letterTwo);
    }

    @DisplayName("Letter with different documents are not same")
    @Test
    void testLetterWithDifferentDocumentsAreNotSame() {
        Letter letterOne = new Letter(getDocuments(), "test");

        Map<String, Object> values = new HashMap<>();
        values.put("name", "Laura");
        Document documentOne = new Document("template_1", values);
        Document documentTwo = new Document("template_2", values);
        Letter letterTwo = new Letter(Arrays.asList(documentOne, documentTwo), "test");
        assertThat(letterOne).isNotEqualTo(letterTwo);
    }

    @DisplayName("Two Letters have same hashcode")
    @Test
    void testTwoLettersHaveSameHashCode() {
        Letter letterOne = new Letter(getDocuments(), "test");
        Letter letterTwo = new Letter(getDocuments(), "test");
        assertThat(letterOne.hashCode()).isEqualTo(letterTwo.hashCode());
    }

    @DisplayName("Two letters with different type dont have same hasCode")
    @Test
    void testTwoDifferentLettersHashCodeDoesNotMatch() {
        Letter letterOne = new Letter(getDocuments(), "test");
        Letter letterTwo = new Letter(getDocuments(), "not_match");
        assertThat(letterOne.hashCode()).isNotEqualTo(letterTwo.hashCode());
    }


    private  List<Document> getDocuments() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", "John");
        Document documentOne = new Document("template_1", values);
        Document documentTwo = new Document("template_2", values);
        return Arrays.asList(documentOne, documentTwo);
    }
}