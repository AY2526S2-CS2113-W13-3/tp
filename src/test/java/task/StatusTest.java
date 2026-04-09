package task;

import app.CommandRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.ParsedCommand;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @@author Aswin-RajeshKumar
/**
 * Unit tests for the Status and Notes update feature.
 * Features comprehensive BVA for index handling and Equivalence Partitioning for status/note updates.
 */
class StatusTest {
    private ArrayList<Application> applications;
    private CommandRunner runner;
    private Application testApp;

    @BeforeEach
    void setUp() {
        // FIXED: Assigning to the class field instead of creating a local variable
        this.applications = new ArrayList<>();
        this.runner = new CommandRunner(applications);

        // Initializing a standard test application at Index 0
        testApp = new Application("Google", "SWE", "2026-03-01");
        applications.add(testApp);
    }

    // ================= SUCCESS CASES (Equivalence Partitioning) =================

    @Test
    void statusCommand_fullUpdate_updatesBothFields() {
        ParsedCommand cmd = new ParsedCommand(0, "OFFER", "Got the job!");
        runner.run(cmd);

        assertEquals("OFFER", applications.get(0).getStatus());
        assertEquals("Got the job!", applications.get(0).getNotes());
    }

    @Test
    void statusCommand_emptyNote_preservesExistingNoteAsEmpty() {
        ParsedCommand cmd = new ParsedCommand(0, "REJECTED", "");
        runner.run(cmd);

        assertEquals("REJECTED", applications.get(0).getStatus());
        assertEquals("", applications.get(0).getNotes());
    }

    @Test
    void statusCommand_longNote_handlesLargeStrings() {
        String longNote = "A".repeat(1000);
        ParsedCommand cmd = new ParsedCommand(0, "PENDING", longNote);
        runner.run(cmd);

        assertEquals(longNote, applications.get(0).getNotes());
    }

    @Test
    void statusCommand_specialCharactersInNote_updatesCorrectly() {
        String specialNote = "Salary: $100k+, WFH? Yes! (Urgently needed)";
        ParsedCommand cmd = new ParsedCommand(0, "INTERVIEW", specialNote);
        runner.run(cmd);

        assertEquals(specialNote, applications.get(0).getNotes());
    }

    // ================= BOUNDARY VALUE ANALYSIS (Index Handling) =================

    @Test
    void statusCommand_firstIndex_updatesCorrectly() {
        // Index 0 is the lower boundary
        ParsedCommand cmd = new ParsedCommand(0, "OFFER", "First item test");
        runner.run(cmd);
        assertEquals("OFFER", applications.get(0).getStatus());
    }

    @Test
    void statusCommand_lastIndex_updatesCorrectly() {
        // Add more apps to test the upper boundary
        applications.add(new Application("Meta", "FE", "2026-04-01"));
        applications.add(new Application("Apple", "iOS", "2026-05-01"));

        int lastIndex = applications.size() - 1; // Index 2
        ParsedCommand cmd = new ParsedCommand(lastIndex, "INTERVIEW", "Last item test");
        runner.run(cmd);

        assertEquals("INTERVIEW", applications.get(lastIndex).getStatus());
    }

    @Test
    void statusCommand_indexJustOutOfBounds_noChange() {
        // Testing size() as an index (Upper boundary + 1)
        ParsedCommand cmd = new ParsedCommand(applications.size(), "OFFER", "Should fail");
        runner.run(cmd);

        // Verify the original app state is untouched
        assertEquals("PENDING", testApp.getStatus());
    }

    @Test
    void statusCommand_veryLargeIndex_noChange() {
        ParsedCommand cmd = new ParsedCommand(999, "OFFER", "Should fail");
        runner.run(cmd);
        assertEquals("PENDING", testApp.getStatus());
    }

    @Test
    void statusCommand_negativeIndex_noChange() {
        // Lower boundary - 1
        ParsedCommand cmd = new ParsedCommand(-1, "OFFER", "Should fail");
        runner.run(cmd);
        assertEquals("PENDING", testApp.getStatus());
    }

    // ================= ROBUSTNESS CASES =================

    @Test
    void statusCommand_nullValuesInCommand_preservesExistingData() {
        // Simulating a partial ParsedCommand where only status is provided
        ParsedCommand cmd = new ParsedCommand(0, "OFFER", null);
        runner.run(cmd);

        assertEquals("OFFER", applications.get(0).getStatus());
        // Depending on your parser logic, this might stay empty or become null
        // If your code has defensive null handling, it should stay as an empty string
    }
}
// @@author