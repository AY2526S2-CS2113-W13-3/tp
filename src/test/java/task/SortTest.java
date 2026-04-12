package task;

import app.CommandRunner;
import parser.ParsedCommand;
import parser.CommandType;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SortTest {

    private ArrayList<Application> applications;
    private CommandRunner runner;

    @BeforeEach
    void setUp() {
        applications = new ArrayList<>();
        runner = new CommandRunner(applications);
    }

    @Test
    void sortApplications_emptyList_printsNoApplicationsMessage() {
        ParsedCommand cmd = new ParsedCommand(CommandType.SORT);
        runner.run(cmd);

        assertTrue(applications.isEmpty());
    }

    @Test
    void sortApplications_nonEmptyList_sortsByDate() {
        Application app1 = new Application("Apple", "iOS", "2025-01-01");
        Application app2 = new Application("Microsoft", "Backend", "2025-02-01");

        applications.add(app2);
        applications.add(app1);

        ParsedCommand cmd = new ParsedCommand(CommandType.SORT);
        runner.run(cmd);

        assertEquals(app1, applications.get(0));
    }

    @Test
    void sortApplications_sortByCompany_ordersAlphabetically() {
        Application app1 = new Application("Zebra", "Role", "2025-01-01");
        Application app2 = new Application("Apple", "Role", "2025-02-01");

        applications.add(app1);
        applications.add(app2);

        ParsedCommand cmd = new ParsedCommand(CommandType.SORT, "company");
        runner.run(cmd);

        assertEquals(app2, applications.get(0));
        assertEquals(app1, applications.get(1));
    }

    @Test
    void sortApplications_invalidField_doesNotReorder() {
        Application app1 = new Application("Zebra", "Role", "2025-01-01");
        Application app2 = new Application("Apple", "Role", "2025-02-01");
        applications.add(app1);
        applications.add(app2);

        ParsedCommand cmd = new ParsedCommand(CommandType.SORT, "hi");
        runner.run(cmd);

        assertEquals(app1, applications.get(0));
        assertEquals(app2, applications.get(1));
    }
}
