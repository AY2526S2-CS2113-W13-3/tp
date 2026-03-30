package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for IndustryTag class.
 * Tests tag normalization, immutability, and equals/hashCode behavior.
 *
 * @@author Yan Xiangyu
 */
class IndustryTagTest {

    @Test
    void constructor_normalizesTagToUppercase() {
        IndustryTag tag = new IndustryTag("tech");
        assertEquals("TECH", tag.getTagName());
    }

    @Test
    void constructor_normalizesMixedCaseToUppercase() {
        IndustryTag tag = new IndustryTag("FiNaNcE");
        assertEquals("FINANCE", tag.getTagName());
    }

    @Test
    void constructor_trimsWhitespace() {
        IndustryTag tag = new IndustryTag("  healthcare  ");
        assertEquals("HEALTHCARE", tag.getTagName());
    }

    @Test
    void constructor_handlesLeadingAndTrailingSpaces() {
        IndustryTag tag = new IndustryTag("   tech   ");
        assertEquals("TECH", tag.getTagName());
    }

    @Test
    void constructor_emptyTagName_throwsAssertionError() {
        // Using assertThrows to verify assertion failure
        assertThrows(AssertionError.class, () -> new IndustryTag(""));
    }

    @Test
    void constructor_nullTagName_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new IndustryTag(null));
    }

    @Test
    void constructor_whitespaceOnly_throwsAssertionError() {
        assertThrows(AssertionError.class, () -> new IndustryTag("   "));
    }

    @Test
    void equals_sameTagName_returnsTrue() {
        IndustryTag tag1 = new IndustryTag("tech");
        IndustryTag tag2 = new IndustryTag("TECH");
        assertEquals(tag1, tag2);
    }

    @Test
    void equals_differentTagName_returnsFalse() {
        IndustryTag tag1 = new IndustryTag("tech");
        IndustryTag tag2 = new IndustryTag("finance");
        assertNotEquals(tag1, tag2);
    }

    @Test
    void equals_sameObject_returnsTrue() {
        IndustryTag tag = new IndustryTag("tech");
        assertEquals(tag, tag);
    }

    @Test
    void equals_null_returnsFalse() {
        IndustryTag tag = new IndustryTag("tech");
        assertNotEquals(null, tag);
    }

    @Test
    void hashCode_sameTagName_returnsSameHash() {
        IndustryTag tag1 = new IndustryTag("tech");
        IndustryTag tag2 = new IndustryTag("TECH");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void hashCode_differentTagName_returnsDifferentHash() {
        IndustryTag tag1 = new IndustryTag("tech");
        IndustryTag tag2 = new IndustryTag("finance");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void toString_returnsUppercaseTagName() {
        IndustryTag tag = new IndustryTag("tech");
        assertEquals("TECH", tag.toString());
    }

    @Test
    void toString_afterNormalization_returnsUppercase() {
        IndustryTag tag = new IndustryTag("  artificial intelligence  ");
        assertEquals("ARTIFICIAL INTELLIGENCE", tag.toString());
    }
}