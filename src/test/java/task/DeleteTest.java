package task;

import app.CommandRunner;
import parser.ParsedCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeleteTest {

    private ArrayList<Application> applications;
    private CommandRunner runner;

    @BeforeEach
    public void setUp() {
        applications = new ArrayList<>();
        runner = new CommandRunner(applications);
    }

    @Test
    public void deleteApplication_validIndex_sizeDecreasesByOne() {
        applications.add(new Application("Google", "Software Engineer", "2026-03-01"));
        applications.add(new Application("Apple", "Data Analyst", "2026-03-02"));

        // Parse command manually
        ParsedCommand cmd = new ParsedCommand(0); // index 0 = first application
        runner.run(cmd);

        // Only 1 application should remain
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
    public void deleteApplication_deleteLastItem_listBecomesEmpty() {
        Application onlyApplication = new Application("Microsoft", "UX Designer", "2026-03-03");
        applications.add(onlyApplication);

        ParsedCommand cmd = new ParsedCommand(0); // delete first (and only) application
        runner.run(cmd);

        assertEquals(0, applications.size());
    }

    @Test
    public void deleteApplication_invalidIndex_doesNotCrash() {
        applications.add(new Application("Google", "Software Engineer", "2026-03-01"));

        // index 5 is out of bounds, run should return true but not remove anything
        ParsedCommand cmd = new ParsedCommand(5);
        boolean continueRunning = runner.run(cmd);

        // List remains unchanged
        assertEquals(1, applications.size());
        // Runner should continue the program
        assertEquals(true, continueRunning);
    }

    @Test
    public void deleteApplication_fromEmptyList_invalidIndexHandled() {
        // deleting index 0 from empty list
        ParsedCommand cmd = new ParsedCommand(0);
        boolean continueRunning = runner.run(cmd);

        assertEquals(0, applications.size());
        assertEquals(true, continueRunning);
    }
}
