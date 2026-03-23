package jobpilot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Add;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JobPilotTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        outContent.reset();
    }

    @Test
    public void sortApplications_emptyList_printsNoApplicationsMessage() {
        ArrayList<Add> emptyApps = new ArrayList<>();
        JobPilot.sortApplications(emptyApps);

        String output = outContent.toString().trim();
        assertEquals("No applications to sort!", output);
        assertTrue(emptyApps.isEmpty());
    }

    @Test
    public void sortApplications_singleElementList_remainsUnchanged() {
        Add app = new Add("Google", "Software Engineer", "2025-01-10");
        ArrayList<Add> singleApps = new ArrayList<>();
        singleApps.add(app);

        JobPilot.sortApplications(singleApps);

        String output = outContent.toString().trim();
        assertTrue(output.contains("Sorted by submission date!"));
        assertTrue(output.contains("1. Google | Software Engineer | 2025-01-10 | Pending"));
        assertEquals(app, singleApps.get(0));
    }

    @Test
    public void sortApplications_nonEmptyList_sortsByDate() {
        Add app1 = new Add("Apple", "iOS Developer", "2025-01-01");
        Add app2 = new Add("Microsoft", "Backend Engineer", "2025-02-01");
        Add app3 = new Add("Amazon", "DevOps Engineer", "2025-03-01");

        ArrayList<Add> unsortedApps = new ArrayList<>();
        unsortedApps.add(app3);
        unsortedApps.add(app1);
        unsortedApps.add(app2);

        JobPilot.sortApplications(unsortedApps);

        assertEquals(app1, unsortedApps.get(0));
        assertEquals(app2, unsortedApps.get(1));
        assertEquals(app3, unsortedApps.get(2));

        String output = outContent.toString().trim();
        assertTrue(output.contains("Sorted by submission date!"));
        assertTrue(output.contains("1. Apple | iOS Developer | 2025-01-01 | Pending"));
        assertTrue(output.contains("2. Microsoft | Backend Engineer | 2025-02-01 | Pending"));
        assertTrue(output.contains("3. Amazon | DevOps Engineer | 2025-03-01 | Pending"));
    }

    @Test
    public void listApplications_emptyList_printsNoApplicationMessage() {
        ArrayList<Add> emptyApps = new ArrayList<>();

        JobPilot.listApplications(emptyApps);

        String output = outContent.toString().trim();
        assertEquals("There is no application yet.", output);
    }

    @Test
    public void listApplications_singleApplication_printsApplication() {
        Add app = new Add("Google", "Software Engineer", "2025-01-10");
        ArrayList<Add> apps = new ArrayList<>();
        apps.add(app);

        JobPilot.listApplications(apps);

        String output = outContent.toString().trim();

        assertTrue(output.contains("Here are your applications:"));
        assertTrue(output.contains("1. Google | Software Engineer | 2025-01-10 | Pending"));
    }

    @Test
    public void listApplications_multipleApplications_printsAllApplications() {
        Add app1 = new Add("Google", "Software Engineer", "2025-01-10");
        Add app2 = new Add("Amazon", "Data Analyst", "2025-02-10");

        ArrayList<Add> apps = new ArrayList<>();
        apps.add(app1);
        apps.add(app2);

        JobPilot.listApplications(apps);

        String output = outContent.toString().trim();

        assertTrue(output.contains("1. Google | Software Engineer | 2025-01-10 | Pending"));
        assertTrue(output.contains("2. Amazon | Data Analyst | 2025-02-10 | Pending"));
    }

    @Test
    public void searchByCompany_noMatches_printsNoApplicationsFound() {
        ArrayList<Add> apps = new ArrayList<>();
        apps.add(new Add("Google", "Software Engineer", "2025-01-10"));
        apps.add(new Add("Amazon", "Data Analyst", "2025-02-10"));

        JobPilot.searchByCompany(apps, "search Microsoft");

        String output = outContent.toString().trim();
        assertTrue(output.contains("No applications found for company: Microsoft"));
    }

    @Test
    public void searchByCompany_exactMatch_findsApplication() {
        ArrayList<Add> apps = new ArrayList<>();
        apps.add(new Add("Google", "Software Engineer", "2025-01-10"));
        apps.add(new Add("Amazon", "Data Analyst", "2025-02-10"));

        JobPilot.searchByCompany(apps, "search Google");

        String output = outContent.toString().trim();
        assertTrue(output.contains("Found 1 application(s) matching 'Google'"));
        assertTrue(output.contains("Google | Software Engineer | 2025-01-10 | Pending"));
        assertFalse(output.contains("Amazon"));
    }

    @Test
    public void searchByCompany_partialMatch_findsApplication() {
        ArrayList<Add> apps = new ArrayList<>();
        apps.add(new Add("Google", "Software Engineer", "2025-01-10"));
        apps.add(new Add("GoGoTravel", "Product Manager", "2025-02-10"));
        apps.add(new Add("Amazon", "Data Analyst", "2025-03-10"));

        JobPilot.searchByCompany(apps, "search Go");

        String output = outContent.toString().trim();
        assertTrue(output.contains("Found 2 application(s) matching 'Go'"));
        assertTrue(output.contains("Google"));
        assertTrue(output.contains("GoGoTravel"));
        assertFalse(output.contains("Amazon"));
    }

    @Test
    public void searchByCompany_caseInsensitive_findsApplication() {
        ArrayList<Add> apps = new ArrayList<>();
        apps.add(new Add("Google", "Software Engineer", "2025-01-10"));
        apps.add(new Add("Amazon", "Data Analyst", "2025-02-10"));

        JobPilot.searchByCompany(apps, "search GOOGLE");

        String output = outContent.toString().trim();
        assertTrue(output.contains("Found 1 application(s) matching 'GOOGLE'"));
        assertTrue(output.contains("Google"));
    }

    @Test
    public void searchByCompany_emptySearchTerm_printsErrorMessage() {
        ArrayList<Add> apps = new ArrayList<>();
        apps.add(new Add("Google", "Software Engineer", "2025-01-10"));

        JobPilot.searchByCompany(apps, "search ");

        String output = outContent.toString().trim();
        assertTrue(output.contains("Please provide a company name to search"));
    }

    @Test
    public void searchByCompany_emptyList_printsNoApplicationsMessage() {
        ArrayList<Add> emptyApps = new ArrayList<>();

        JobPilot.searchByCompany(emptyApps, "search Google");
        String output = outContent.toString().trim();
        assertEquals("No applications to search!", output);
    }
}