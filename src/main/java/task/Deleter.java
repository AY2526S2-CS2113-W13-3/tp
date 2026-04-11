package task;

import exception.JobPilotException;
import java.util.ArrayList;

/**
 * Handles the deletion of a given application from the list.
 */
public class Deleter {
    /**
     * Deletes an application from the list by parsing the index provided by the user.
     *
     * @param applications The list storing all job applications.
     * @param deleteIndex The index of the application to be removed.
     * @return The application that was successfully removed.
     * @throws JobPilotException If the index provided is not a valid integer or is out of range.
     */
    public static Application deleteApplication(ArrayList<Application> applications, int deleteIndex)
            throws JobPilotException {
            if (deleteIndex < 0 || deleteIndex >= applications.size()) {
                throw new JobPilotException("Invalid application number!");
            }

            Application removed = applications.remove(deleteIndex);
            assert removed != null : "The application was not successfully deleted. Please try again!";

            return removed;
    }
}
