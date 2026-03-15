package task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import exception.JobPilotException;
import jobpilot.JobPilot;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddTest {

    private ArrayList<Add> applications;

    @BeforeEach
    public void setUp() {
        applications = new ArrayList<>();
    }

    @Test
    public void addApplication_validInput_sizeIncreasesByOne() throws JobPilotException {
        JobPilot.addApplication(applications, "add c/Google p/Software Engineer d/2024-09-12");

        assertEquals(1, applications.size());
    }

    @Test
    public void addApplication_validInput_correctApplicationAdded() throws JobPilotException {
        JobPilot.addApplication(applications, "add c/Google p/Software Engineer d/2024-09-12");

        Add added = applications.get(0);
        assertEquals("Google", added.getCompany());
        assertEquals("Software Engineer", added.getPosition());
        assertEquals("2024-09-12", added.getDate());
        assertEquals("Pending", added.getStatus());
    }

    @Test
    public void addApplication_multipleApplications_allAddedCorrectly() throws JobPilotException {
        JobPilot.addApplication(applications, "add c/Google p/SWE d/2024-09-12");
        JobPilot.addApplication(applications, "add c/Meta p/PM d/2024-09-15");
        JobPilot.addApplication(applications, "add c/Amazon p/SDE d/2024-09-20");

        assertEquals(3, applications.size());

        Add first = applications.get(0);
        assertEquals("Google", first.getCompany());
        assertEquals("SWE", first.getPosition());
        assertEquals("2024-09-12", first.getDate());

        Add second = applications.get(1);
        assertEquals("Meta", second.getCompany());
        assertEquals("PM", second.getPosition());
        assertEquals("2024-09-15", second.getDate());

        Add third = applications.get(2);
        assertEquals("Amazon", third.getCompany());
        assertEquals("SDE", third.getPosition());
        assertEquals("2024-09-20", third.getDate());
    }

    @Test
    public void addApplication_missingCompany_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add p/Software Engineer d/2024-09-12");
        });

        assertEquals("Missing required fields! Use: add c/COMPANY p/POSITION d/DATE", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_missingPosition_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add c/Google d/2024-09-12");
        });

        assertEquals("Missing required fields! Use: add c/COMPANY p/POSITION d/DATE", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_missingDate_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add c/Google p/Software Engineer");
        });

        assertEquals("Missing required fields! Use: add c/COMPANY p/POSITION d/DATE", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_missingAllFields_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add");
        });

        assertEquals("Missing required fields! Use: add c/COMPANY p/POSITION d/DATE", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_wrongOrder_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add p/Software Engineer c/Google d/2024-09-12");
        });

        assertEquals("Wrong order! Use: c/COMPANY then p/POSITION then d/DATE", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_completelyWrongOrder_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add d/2024-09-12 p/Software Engineer c/Google");
        });

        assertEquals("Wrong order! Use: c/COMPANY then p/POSITION then d/DATE", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_emptyCompany_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add c/ p/Software Engineer d/2024-09-12");
        });

        assertEquals("Fields cannot be empty!", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_emptyPosition_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add c/Google p/ d/2024-09-12");
        });

        assertEquals("Fields cannot be empty!", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_emptyDate_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add c/Google p/Software Engineer d/");
        });

        assertEquals("Fields cannot be empty!", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_emptyFields_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            JobPilot.addApplication(applications, "add c/ p/ d/");
        });

        assertEquals("Fields cannot be empty!", exception.getMessage());
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_invalidDateFormat_applicationNotAdded() throws JobPilotException {
        // This should print error but not throw exception
        JobPilot.addApplication(applications, "add c/Google p/Software Engineer d/2024/09/12");

        // Application should not be added due to invalid date
        assertEquals(0, applications.size());
    }

    @Test
    public void addApplication_verifyToStringFormat() throws JobPilotException {
        JobPilot.addApplication(applications, "add c/Google p/SWE d/2024-09-12");

        Add app = applications.get(0);
        String expectedString = "Google | SWE | 2024-09-12 | Pending";
        assertEquals(expectedString, app.toString());
    }

    @Test
    public void addApplication_statusDefaultsToPending() throws JobPilotException {
        JobPilot.addApplication(applications, "add c/Google p/SWE d/2024-09-12");

        Add app = applications.get(0);
        assertEquals("Pending", app.getStatus());
    }
}
