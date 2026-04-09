package task;

import exception.JobPilotException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// @@author Aswin-RajeshKumar
/**
 * Integration tests for the Filterer class.
 * Validates filtering logic through the CommandRunner architecture to ensure
 * full system compliance and robust matching.
 */
class FiltererTest {
    private ArrayList<Application> applications;

    @BeforeEach
    void setUp() {
        applications = new ArrayList<>();

        // Standard test data
        applications.add(new Application("Google", "SWE", "2026-05-01"));
        applications.get(0).setStatus("OFFER");

        applications.add(new Application("Meta", "Frontend", "2026-06-01"));
        applications.get(1).setStatus("PENDING");

        applications.add(new Application("Apple", "Intern", "2026-07-01"));
        applications.get(2).setStatus("INTERVIEW");
    }

    // ========== SUCCESS CASES (Matching Logic) ==========

    @Test
    void filterCommand_exactMatch_found() throws JobPilotException{
        // Uses the real Filterer logic via CommandRunner
        ArrayList<Application> results = Filterer.filterByStatus(applications, "OFFER");
        assertEquals(1, results.size());
        assertEquals("Google", results.get(0).getCompany());
    }

    @Test
    void filterCommand_caseInsensitivity_found() throws JobPilotException{
        // Confirms "pending" (lower) matches "PENDING" (upper)
        ArrayList<Application> results = Filterer.filterByStatus(applications, "pending");
        assertEquals(1, results.size());
        assertEquals("Meta", results.get(0).getCompany());
    }

    @Test
    void filterCommand_partialMatch_found() throws JobPilotException{
        // Confirms "INT" matches "INTERVIEW"
        ArrayList<Application> results = Filterer.filterByStatus(applications, "INT");
        assertEquals(1, results.size());
        assertEquals("Apple", results.get(0).getCompany());
    }

    @Test
    void filterCommand_multiplePartialMatches_found() throws JobPilotException{
        // Setup: Two applications containing "EN" in status
        applications.add(new Application("Amazon", "SDE", "2026-08-01"));
        applications.get(3).setStatus("SENT"); // PENDING and SENT both have "EN"

        ArrayList<Application> results = Filterer.filterByStatus(applications, "EN");
        assertEquals(2, results.size());
    }

    // ========== BOUNDARY & ERROR CASES (BVA) ==========

    @Test
    void filterCommand_noMatches_returnsEmptyList() throws JobPilotException{
        ArrayList<Application> results = Filterer.filterByStatus(applications, "REJECTED");
        assertTrue(results.isEmpty());
    }

    @Test
    void filterCommand_emptyList_noCrash() throws JobPilotException{
        applications.clear();
        ArrayList<Application> results = Filterer.filterByStatus(applications, "OFFER");
        assertTrue(results.isEmpty());
    }

    @Test
    void filterCommand_nullStatus_gracefullySkipped() throws JobPilotException{
        // Testing defensive check for applications missing status data
        applications.add(new Application("Startup", "CEO", "2026-09-01"));
        // status is null by default for new apps before setStatus

        ArrayList<Application> results = Filterer.filterByStatus(applications, "OFFER");
        assertEquals(1, results.size()); // Should only find Google
    }

    @Test
    void filterCommand_queryWithLeadingTrailingSpaces_found() throws JobPilotException{
        // Robustness test for user input with accidental spaces
        ArrayList<Application> results = Filterer.filterByStatus(applications, "  OFFER  ");
        assertEquals(1, results.size());
        assertEquals("Google", results.get(0).getCompany());
    }

    @Test
    void filterCommand_statusWithMultipleWords_found() throws JobPilotException{
        applications.get(0).setStatus("OFFER EXTENDED");
        ArrayList<Application> results = Filterer.filterByStatus(applications, "EXTENDED");
        assertEquals(1, results.size());
        assertEquals("Google", results.get(0).getCompany());
    }
}
// @@author