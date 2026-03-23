package task;

import exception.JobPilotException;

import java.util.ArrayList;

/**
 * Handles the deletion of given application.
 */
public class Delete {
    /**
     * Deletes an application from the list by parsing the index provided by the user.
     *
     * @param input The full user command (e.g., "delete 2").
     * @param applications The list storing all job applications.
     * @throws JobPilotException If the index provided is not a valid integer.
     */
    public static void deleteApplication(String input, ArrayList<Add> applications) throws JobPilotException {
        try {
            String[] parts = input.split(" ");

            if (parts.length < 2) {
                throw new JobPilotException("Please provide an index. Example: delete 1");
            }

            assert applications != null : "The application list is empty!";

            int deleteIndex = Integer.parseInt(parts[1]) - 1;

            if (deleteIndex < 0 || deleteIndex >= applications.size()) {
                throw new JobPilotException("Invalid application number!");
            }

            Add removed = applications.remove(deleteIndex);

            System.out.println("Deleted application:");
            System.out.println(removed);
            System.out.println("You have " + applications.size() + " application(s) left.");

            assert removed != null : "The application was not successfully deleted. Please try again!";
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid format! Use: delete INDEX");
        }
    }
}
