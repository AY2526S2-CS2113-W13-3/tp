package task;

import exception.JobPilotException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for editing applications using the Edit class.
 * Covers success, error, and edge cases.
 */
class EditorTest {

    private ArrayList<Application> applications;
    private Application testApp;

    @BeforeEach
    void setUp() {
        applications = new ArrayList<>();
        testApp = new Application("Google", "Software Engineer", "2024-09-12");
        applications.add(testApp);
    }

    // ================= SUCCESS CASES =================

    @Test
    void editApplication_updateCompanyOnly_success() throws JobPilotException {
        Editor.editApplication("edit 1 c/Microsoft", applications);
        assertEquals("Microsoft", testApp.getCompany());
        assertEquals("Software Engineer", testApp.getPosition());
        assertEquals("2024-09-12", testApp.getDate());
        assertEquals("PENDING", testApp.getStatus());
    }

    @Test
    void editApplication_updatePositionOnly_success() throws JobPilotException {
        Editor.editApplication("edit 1 p/Senior Engineer", applications);
        assertEquals("Google", testApp.getCompany());
        assertEquals("Senior Engineer", testApp.getPosition());
        assertEquals("2024-09-12", testApp.getDate());
        assertEquals("PENDING", testApp.getStatus());
    }

    @Test
    void editApplication_updateDateOnly_success() throws JobPilotException {
        Editor.editApplication("edit 1 d/2024-12-01", applications);
        assertEquals("Google", testApp.getCompany());
        assertEquals("Software Engineer", testApp.getPosition());
        assertEquals("2024-12-01", testApp.getDate());
        assertEquals("PENDING", testApp.getStatus());
    }

    @Test
    void editApplication_updateStatusOnly_success() throws JobPilotException {
        Editor.editApplication("edit 1 s/INTERVIEW", applications);
        assertEquals("Google", testApp.getCompany());
        assertEquals("Software Engineer", testApp.getPosition());
        assertEquals("2024-09-12", testApp.getDate());
        assertEquals("INTERVIEW", testApp.getStatus());
    }

    @Test
    void editApplication_updateMultipleFields_success() throws JobPilotException {
        Editor.editApplication("edit 1 c/Microsoft p/Senior Engineer d/2024-12-01 s/OFFER", applications);
        assertEquals("Microsoft", testApp.getCompany());
        assertEquals("Senior Engineer", testApp.getPosition());
        assertEquals("2024-12-01", testApp.getDate());
        assertEquals("OFFER", testApp.getStatus());
    }

    @Test
    void editApplication_updateFieldsWithSpaces_success() throws JobPilotException {
        Editor.editApplication("edit 1 c/Amazon Web Services p/Senior Software Engineer", applications);
        assertEquals("Amazon Web Services", testApp.getCompany());
        assertEquals("Senior Software Engineer", testApp.getPosition());
    }

    // ================= ERROR CASES =================

    @Test
    void editApplication_missingIndex_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            Editor.editApplication("edit", applications);
        });
        assertTrue(exception.getMessage().contains("provide an index"));
    }

    @Test
    void editApplication_indexZeroOrTooLarge_throwsException() {
        JobPilotException ex1 = assertThrows(JobPilotException.class, () -> {
            Editor.editApplication("edit 0 c/Google", applications);
        });
        JobPilotException ex2 = assertThrows(JobPilotException.class, () -> {
            Editor.editApplication("edit 99 c/Google", applications);
        });

        assertTrue(ex1.getMessage().contains("Invalid application number"));
        assertTrue(ex2.getMessage().contains("Invalid application number"));
    }

    @Test
    void editApplication_nonNumericIndex_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            Editor.editApplication("edit abc c/Google", applications);
        });
        assertTrue(exception.getMessage().contains("Invalid index"));
    }

    @Test
    void editApplication_noFieldsToUpdate_throwsException() {
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            Editor.editApplication("edit 1", applications);
        });
        assertTrue(exception.getMessage().contains("No valid fields"));
    }

    @Test
    void editApplication_invalidDateFormatOrValue_throwsException() {
        JobPilotException ex1 = assertThrows(JobPilotException.class, () -> {
            Editor.editApplication("edit 1 d/01-01-2024", applications);
        });
        JobPilotException ex2 = assertThrows(JobPilotException.class, () -> {
            Editor.editApplication("edit 1 d/2024-02-30", applications);
        });

        assertTrue(ex1.getMessage().contains("Invalid date"));
        assertTrue(ex2.getMessage().contains("Invalid date"));
    }

    @Test
    void editApplication_emptyFields_throwsException() {
        String[] cmds = {"edit 1 c/", "edit 1 p/", "edit 1 d/", "edit 1 s/"};
        for (String cmd : cmds) {
            JobPilotException exception = assertThrows(JobPilotException.class, () -> {
                Editor.editApplication(cmd, applications);
            });
            assertTrue(exception.getMessage().contains("cannot be empty"));
        }
    }

    // ================= EDGE CASES =================

    @Test
    void editApplication_emptyList_throwsException() {
        applications.clear();
        JobPilotException exception = assertThrows(JobPilotException.class, () -> {
            Editor.editApplication("edit 1 c/Google", applications);
        });
        assertTrue(exception.getMessage().contains("Invalid application number"));
    }

    @Test
    void editApplication_multipleApps_editSecondApp_success() throws JobPilotException {
        Application secondApp = new Application("Meta", "Product Manager", "2024-09-15");
        applications.add(secondApp);

        Editor.editApplication("edit 2 c/Amazon", applications);

        assertEquals("Google", applications.get(0).getCompany());
        assertEquals("Amazon", applications.get(1).getCompany());
    }

    @Test
    void editApplication_whitespaceInCommand_success() throws JobPilotException {
        Editor.editApplication("edit   1   c/Microsoft   ", applications);
        assertEquals("Microsoft", testApp.getCompany());
    }

    @Test
    void editApplication_caseSensitiveFields_preservesCase() throws JobPilotException {
        Editor.editApplication("edit 1 c/GoogleInc", applications);
        assertEquals("GoogleInc", testApp.getCompany());
    }
}