package task;

import app.CommandRunner;
import parser.ParsedCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleterTest {

    private ArrayList<Application> applications;
    private CommandRunner runner;

    @BeforeEach
    public void setUp() {
        applications = new ArrayList<>();
        runner = new CommandRunner(applications);
    }

    @Test
    public void deleteApplication_deleteLastItem_listBecomesEmpty() {
        Application onlyApplication = new Application("Microsoft", "UX Designer", "2026-03-03");
        applications.add(onlyApplication);

        ParsedCommand cmd = new ParsedCommand(0); // delete first (and only) application
        runner.run(cmd);

        assertEquals(0, applications.size());
    }

    @Test
    public void deleteApplication_validIndex_sizeDecreasesByOne() {
        applications.add(new Application("Google", "Software Engineer", "2026-03-01"));
        applications.add(new Application("Apple", "Data Analyst", "2026-03-02"));

        ParsedCommand cmd = new ParsedCommand(0); // index 0 = first application
        runner.run(cmd);

        assertEquals(1, applications.size());
    }

    @Test
    public void deleteApplication_deleteSecondApplication_correctItemRemoved() {
        Application first = new Application("Google", "Software Engineer", "2026-03-01");
        Application second = new Application("Apple", "Data Analyst", "2026-03-02");

        applications.add(first);
        applications.add(second);

        ParsedCommand cmd = new ParsedCommand(1); // delete second application (index 1)
        runner.run(cmd);

        assertEquals(1, applications.size());
        assertEquals(first, applications.get(0));
    }

    @Test
    public void deleteApplication_deleteFirstOfThree_remainingApplicationsShiftCorrectly() {
        Application first = new Application("Google", "Software Engineer", "2026-03-01");
        Application second = new Application("Apple", "Data Analyst", "2026-03-02");
        Application third = new Application("Microsoft", "UX Designer", "2026-03-03");

        applications.add(first);
        applications.add(second);
        applications.add(third);

        ParsedCommand cmd = new ParsedCommand(0);
        runner.run(cmd);

        assertEquals(2, applications.size());
        assertEquals(second, applications.get(0));
        assertEquals(third, applications.get(1));
    }

    @Test
    public void deleteApplication_deleteMiddleOfThree_correctRemainingApplications() {
        Application first = new Application("Google", "Software Engineer", "2026-03-01");
        Application second = new Application("Apple", "Data Analyst", "2026-03-02");
        Application third = new Application("Microsoft", "UX Designer", "2026-03-03");

        applications.add(first);
        applications.add(second);
        applications.add(third);

        ParsedCommand cmd = new ParsedCommand(1);
        runner.run(cmd);

        assertEquals(2, applications.size());
        assertEquals(first, applications.get(0));
        assertEquals(third, applications.get(1));
    }

    @Test
    public void deleteApplication_deleteLastOfThree_correctRemainingApplications() {
        Application first = new Application("Google", "Software Engineer", "2026-03-01");
        Application second = new Application("Apple", "Data Analyst", "2026-03-02");
        Application third = new Application("Microsoft", "UX Designer", "2026-03-03");

        applications.add(first);
        applications.add(second);
        applications.add(third);

        ParsedCommand cmd = new ParsedCommand(2);
        runner.run(cmd);

        assertEquals(2, applications.size());
        assertEquals(first, applications.get(0));
        assertEquals(second, applications.get(1));
    }

    @Test
    public void deleteApplication_fromEmptyList_invalidIndexHandled() {
        ParsedCommand cmd = new ParsedCommand(0);
        boolean continueRunning = runner.run(cmd);

        assertEquals(0, applications.size());
        assertEquals(true, continueRunning);
    }

    @Test
    public void deleteApplication_missingIndex_shouldNotModifyList() {
        applications.add(new Application("Google", "SE", "2026-03-01"));

        // invalid delete (no index)
        ParsedCommand cmd = new ParsedCommand(-999); // assuming invalid index representation
        boolean continueRunning = runner.run(cmd);

        assertEquals(1, applications.size());
        assertEquals(true, continueRunning);
    }

    @Test
    public void deleteApplication_extraArguments_shouldNotModifyList() {
        Application app = new Application("Google", "SE", "2026-03-01");
        applications.add(app);

        // malformed delete command parsed incorrectly
        ParsedCommand cmd = new ParsedCommand(-1); // invalid due to parsing failure
        runner.run(cmd);

        assertEquals(1, applications.size());
        assertEquals(app, applications.get(0));
    }

    @Test
    public void deleteApplication_invalidIndex_doesNotCrash() {
        applications.add(new Application("Google", "Software Engineer", "2026-03-01"));

        ParsedCommand cmd = new ParsedCommand(5);
        boolean continueRunning = runner.run(cmd);

        assertEquals(1, applications.size());
        assertEquals(true, continueRunning);
    }

    @Test
    public void deleteApplication_negativeIndex_doesNotModifyList() {
        applications.add(new Application("Google", "Software Engineer", "2026-03-01"));

        ParsedCommand cmd = new ParsedCommand(-1);
        boolean continueRunning = runner.run(cmd);

        assertEquals(1, applications.size());
        assertEquals(true, continueRunning);
    }

}
