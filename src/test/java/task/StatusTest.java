package task;

import app.CommandRunner;
import exception.JobPilotException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.ParsedCommand;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for updating application status using CommandRunner and ParsedCommand.
 */
class StatusTest {

    private ArrayList<Application> applications;
    private CommandRunner runner;
    private Application testApp;

    @BeforeEach
    void setUp() {
        applications = new ArrayList<>();
        runner = new CommandRunner(applications);

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
        ParsedCommand cmd = new ParsedCommand(0, testApp.getStatus(), "Updated note");
        runner.run(cmd);

        assertEquals(testApp.getStatus(), testApp.getStatus());
        assertEquals("Updated note", testApp.getNotes());
    }

    // ================= ERROR CASES =================

    @Test
    void statusCommand_invalidNegativeIndex_showsError() {
        ParsedCommand cmd = new ParsedCommand(-1, "OFFER", "Note");
        boolean continueFlag = runner.run(cmd);

        // Status and notes should remain unchanged
        assertTrue(continueFlag);
        assertEquals("Pending", testApp.getStatus());
        assertEquals("", testApp.getNotes());
    }

    @Test
    void statusCommand_indexTooLarge_showsError() {
        ParsedCommand cmd = new ParsedCommand(5, "OFFER", "Note");
        runner.run(cmd);

        assertEquals("Pending", testApp.getStatus());
        assertEquals("", testApp.getNotes());
    }
}