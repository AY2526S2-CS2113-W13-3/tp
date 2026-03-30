package task;

import app.CommandRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.ParsedCommand;
import java.util.Set;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// @@author Aswin-RajeshKumar
/**
 * Unit tests for the Status and Notes update feature.
 * Original test cases and boundary value analysis (BVA) authored for v2.0.
 * Refactored to integrate with the CommandRunner architecture.
 */
class StatusTest {

    private ArrayList<Application> applications;
    private CommandRunner runner;
    private Application testApp;

    @BeforeEach
    void setUp() {
        applications = new ArrayList<>();
        runner = new CommandRunner(applications);

        // Initializing a standard test application
        testApp = new Application("Google", "SWE", "2026-03-01");
        applications.add(testApp);
    }

    // ================= SUCCESS CASES =================

    @Test
    void statusCommand_updateStatusAndNotes_success() {
        ParsedCommand cmd = new ParsedCommand(0, "OFFER", "Great news");
        boolean continueFlag = runner.run(cmd);

        assertTrue(continueFlag);
        assertEquals("OFFER", testApp.getStatus());
        assertEquals("Great news", testApp.getNotes());
    }

    @Test
    void statusCommand_updateStatusOnly_success() {
        ParsedCommand cmd = new ParsedCommand(0, "INTERVIEW", "");
        runner.run(cmd);

        assertEquals("INTERVIEW", testApp.getStatus());
        assertEquals("", testApp.getNotes());
    }

    @Test
    void statusCommand_updateNotesOnly_success() {
        // Logic to verify that notes can be updated while status remains unchanged
        ParsedCommand cmd = new ParsedCommand(0, testApp.getStatus(), "Updated note");
        runner.run(cmd);

        assertEquals("Pending", testApp.getStatus()); // Default status
        assertEquals("Updated note", testApp.getNotes());
    }

    // ================= ERROR CASES (BVA) =================

    @Test
    void statusCommand_invalidNegativeIndex_showsError() {
        // Boundary test: Negative index should not crash or modify data
        ParsedCommand cmd = new ParsedCommand(-1, "OFFER", "Note");
        boolean continueFlag = runner.run(cmd);

        assertTrue(continueFlag);
        assertEquals("Pending", testApp.getStatus());
        assertEquals("", testApp.getNotes());
    }

    @Test
    void statusCommand_indexTooLarge_showsError() {
        // Boundary test: Index exceeding list size
        ParsedCommand cmd = new ParsedCommand(5, "OFFER", "Note");
        runner.run(cmd);

        assertEquals("Pending", testApp.getStatus());
        assertEquals("", testApp.getNotes());
    }

    // ================= INDUSTRY TAG TESTS =================
    // @@author Yan Xiangyu

    @Test
    void addIndustryTag_toApplication_success() {
        Application app = new Application("Google", "SWE", "2025-01-01");
        IndustryTag tag = new IndustryTag("tech");

        app.addIndustryTag(tag);

        assertTrue(app.getIndustryTags().contains(tag));
        assertEquals(1, app.getIndustryTags().size());
    }

    @Test
    void removeIndustryTag_fromApplication_success() {
        Application app = new Application("Google", "SWE", "2025-01-01");
        IndustryTag tag = new IndustryTag("tech");
        app.addIndustryTag(tag);

        app.removeIndustryTag(tag);

        assertFalse(app.getIndustryTags().contains(tag));
        assertTrue(app.getIndustryTags().isEmpty());
    }

    @Test
    void getIndustryTags_returnsUnmodifiableSet() {
        Application app = new Application("Google", "SWE", "2025-01-01");
        app.addIndustryTag(new IndustryTag("tech"));

        Set<IndustryTag> tags = app.getIndustryTags();

        assertThrows(UnsupportedOperationException.class, () -> tags.clear());
    }

    @Test
    void toString_withIndustryTags_displaysTags() {
        Application app = new Application("Google", "SWE", "2025-01-01");
        app.addIndustryTag(new IndustryTag("tech"));
        app.addIndustryTag(new IndustryTag("finance"));

        String output = app.toString();

        assertTrue(output.contains("Tags: [TECH, FINANCE]") ||
                output.contains("Tags: [FINANCE, TECH]"));
    }
}
// @@author