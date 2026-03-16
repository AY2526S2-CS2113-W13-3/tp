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
}