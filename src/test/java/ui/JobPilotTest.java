package ui;

import app.CommandRunner;
import parser.ParsedCommand;
import parser.CommandType;
import task.Application;
import task.IndustryTag;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for JobPilot commands using CommandRunner and ParsedCommand.
 * Covers list, sort, and search behavior.
 */
public class JobPilotTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    private ArrayList<Application> applications;
    private CommandRunner runner;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        applications = new ArrayList<>();
        runner = new CommandRunner(applications);
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        outContent.reset();
    }

    // ================= LIST TESTS =================

    @Test
    void listApplications_emptyList_printsNoApplicationMessage() {
        ParsedCommand cmd = new ParsedCommand(CommandType.LIST);
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("There is no application yet."));
    }

    @Test
    void listApplications_singleApplication_printsApplication() {
        Application app = new Application("Google", "Software Engineer", "2025-01-10");
        applications.add(app);

        ParsedCommand cmd = new ParsedCommand(CommandType.LIST);
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("Here are your applications:"));
        assertTrue(output.contains("1. Google | Software Engineer | 2025-01-10 | Pending"));
    }

    @Test
    void listApplications_multipleApplications_printsAllApplications() {
        Application app1 = new Application("Google", "Software Engineer", "2025-01-10");
        Application app2 = new Application("Amazon", "Data Analyst", "2025-02-10");
        applications.add(app1);
        applications.add(app2);

        ParsedCommand cmd = new ParsedCommand(CommandType.LIST);
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("1. Google | Software Engineer | 2025-01-10 | Pending"));
        assertTrue(output.contains("2. Amazon | Data Analyst | 2025-02-10 | Pending"));
    }

    // ================= SORT TESTS =================

    @Test
    void sortApplications_emptyList_printsNoApplicationsMessage() {
        ParsedCommand cmd = new ParsedCommand(CommandType.SORT);
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("There is no application yet."));
        assertTrue(applications.isEmpty());
    }

    @Test
    void sortApplications_singleElementList_remainsUnchanged() {
        Application app = new Application("Google", "Software Engineer", "2025-01-10");
        applications.add(app);

        ParsedCommand cmd = new ParsedCommand(CommandType.SORT);
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("Sorted by submission date!"));
        assertTrue(output.contains("1. Google | Software Engineer | 2025-01-10 | Pending"));
        assertEquals(app, applications.get(0));
    }

    @Test
    void sortApplications_nonEmptyList_sortsByDate() {
        Application app1 = new Application("Apple", "iOS Developer", "2025-01-01");
        Application app2 = new Application("Microsoft", "Backend Engineer", "2025-02-01");
        Application app3 = new Application("Amazon", "DevOps Engineer", "2025-03-01");

        applications.add(app3);
        applications.add(app1);
        applications.add(app2);

        ParsedCommand cmd = new ParsedCommand(CommandType.SORT);
        runner.run(cmd);

        assertEquals(app1, applications.get(0));
        assertEquals(app2, applications.get(1));
        assertEquals(app3, applications.get(2));

        String output = outContent.toString().trim();
        assertTrue(output.contains("Sorted by submission date!"));
        assertTrue(output.contains("1. Apple | iOS Developer | 2025-01-01 | Pending"));
        assertTrue(output.contains("2. Microsoft | Backend Engineer | 2025-02-01 | Pending"));
        assertTrue(output.contains("3. Amazon | DevOps Engineer | 2025-03-01 | Pending"));
    }

    // ================= SEARCH TESTS =================

    @Test
    void searchByCompany_noMatches_printsNoApplicationsFound() {
        applications.add(new Application("Google", "Software Engineer", "2025-01-10"));
        applications.add(new Application("Amazon", "Data Analyst", "2025-02-10"));

        ParsedCommand cmd = new ParsedCommand("Microsoft"); // search term
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("No applications found for company: Microsoft"));
    }

    @Test
    void searchByCompany_exactMatch_findsApplication() {
        applications.add(new Application("Google", "Software Engineer", "2025-01-10"));
        applications.add(new Application("Amazon", "Data Analyst", "2025-02-10"));

        ParsedCommand cmd = new ParsedCommand("Google");
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("Found 1 application(s) matching 'Google'"));
        assertTrue(output.contains("Google | Software Engineer | 2025-01-10 | Pending"));
        assertFalse(output.contains("Amazon"));
    }

    @Test
    void searchByCompany_partialMatch_findsApplication() {
        applications.add(new Application("Google", "Software Engineer", "2025-01-10"));
        applications.add(new Application("GoGoTravel", "Product Manager", "2025-02-10"));
        applications.add(new Application("Amazon", "Data Analyst", "2025-03-10"));

        ParsedCommand cmd = new ParsedCommand("Go");
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("Found 2 application(s) matching 'Go'"));
        assertTrue(output.contains("Google"));
        assertTrue(output.contains("GoGoTravel"));
        assertFalse(output.contains("Amazon"));
    }

    @Test
    void searchByCompany_caseInsensitive_findsApplication() {
        applications.add(new Application("Google", "Software Engineer", "2025-01-10"));
        applications.add(new Application("Amazon", "Data Analyst", "2025-02-10"));

        ParsedCommand cmd = new ParsedCommand("GOOGLE");
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("Found 1 application(s) matching 'GOOGLE'"));
        assertTrue(output.contains("Google"));
    }

    @Test
    void searchByCompany_emptyList_printsNoApplicationsMessage() {
        ParsedCommand cmd = new ParsedCommand("Google");
        runner.run(cmd);

        String output = outContent.toString().trim();
        assertTrue(output.contains("No applications to search!"));
    }

}