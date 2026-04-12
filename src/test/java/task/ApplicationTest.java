package task;

import app.CommandRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.ParsedCommand;
import parser.Parser;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for adding applications using CommandRunner and Parser.
 */
class ApplicationTest {

    private ArrayList<Application> applications;
    private CommandRunner runner;

    @BeforeEach
    public void setUp() {
        applications = new ArrayList<>();
        runner = new CommandRunner(applications);
    }

    @Test
    public void run_addCommand_validInput_sizeIncreasesByOne() {
        ParsedCommand cmd = Parser.parse("add c/Google p/Software Engineer d/2024-09-12");
        runner.run(cmd);
        assertEquals(1, applications.size());
    }

    @Test
    public void run_addCommand_correctApplicationAdded() {
        ParsedCommand cmd = Parser.parse("add c/Google p/Software Engineer d/2024-09-12");
        runner.run(cmd);

        Application added = applications.get(0);
        assertEquals("Google", added.getCompany());
        assertEquals("Software Engineer", added.getPosition());
        assertEquals("2024-09-12", added.getDate());
        assertEquals("PENDING", added.getStatus());
    }

    @Test
    public void run_addCommand_multipleApplications_allAddedCorrectly() {
        runner.run(Parser.parse("add c/Google p/SWE d/2024-09-12"));
        runner.run(Parser.parse("add c/Meta p/PM d/2024-09-15"));
        runner.run(Parser.parse("add c/Amazon p/SDE d/2024-09-20"));

        assertEquals(3, applications.size());

        Application first = applications.get(0);
        assertEquals("Google", first.getCompany());
        assertEquals("SWE", first.getPosition());
        assertEquals("2024-09-12", first.getDate());

        Application second = applications.get(1);
        assertEquals("Meta", second.getCompany());
        assertEquals("PM", second.getPosition());
        assertEquals("2024-09-15", second.getDate());

        Application third = applications.get(2);
        assertEquals("Amazon", third.getCompany());
        assertEquals("SDE", third.getPosition());
        assertEquals("2024-09-20", third.getDate());
    }

    @Test
    public void run_addCommand_missingCompany_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add p/Software Engineer d/2024-09-12");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_missingPosition_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add c/Google d/2024-09-12");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_missingDate_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add c/Google p/Software Engineer");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_wrongOrder_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add p/Software Engineer c/Google d/2024-09-12");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_completelyWrongOrder_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add d/2024-09-12 p/Software Engineer c/Google");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_emptyCompany_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add c/ p/Software Engineer d/2024-09-12");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_emptyPosition_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add c/Google p/ d/2024-09-12");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_emptyDate_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add c/Google p/Software Engineer d/");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_emptyFields_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add c/ p/ d/");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_invalidDateFormat_applicationNotAdded() {
        ParsedCommand cmd = Parser.parse("add c/Google p/Software Engineer d/2024/09/12");
        runner.run(cmd);
        assertEquals(0, applications.size());
    }

    @Test
    public void run_addCommand_verifyToStringFormat() {
        ParsedCommand cmd = Parser.parse("add c/Google p/SWE d/2024-09-12");
        runner.run(cmd);

        Application app = applications.get(0);
        String expectedString = "Google | SWE | 2024-09-12 | PENDING";
        assertEquals(expectedString, app.toString());
    }

    @Test
    public void run_addCommand_statusDefaultsToPending() {
        ParsedCommand cmd = Parser.parse("add c/Google p/SWE d/2024-09-12");
        runner.run(cmd);

        Application app = applications.get(0);
        assertEquals("PENDING", app.getStatus());
    }

    @Test
    public void run_addCommand_duplicateApplication_notAdded() {
        runner.run(Parser.parse("add c/AMD p/Engineer d/2026-01-10"));
        assertEquals(1, applications.size());
        runner.run(Parser.parse("add c/AMD p/Engineer d/2026-01-10"));
        assertEquals(1, applications.size());
    }

    @Test
    public void run_addCommand_duplicateApplicationDifferentCase_notAdded() {
        runner.run(Parser.parse("add c/Google p/Software Engineer d/2024-09-12"));
        assertEquals(1, applications.size());
        runner.run(Parser.parse("add c/GOOGLE p/SOFTWARE ENGINEER d/2024-09-12"));
        assertEquals(1, applications.size());
    }
}
