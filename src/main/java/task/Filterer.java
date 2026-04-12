package task;

import exception.JobPilotException;
import ui.Ui;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

//@@author Aswin-RajeshKumar
/**
 * Utility class to handle filtering of job applications.
 * Implemented for v2.0 milestone.
 */
public class Filterer {

    static {
        Logger.getLogger(Filterer.class.getName()).setLevel(Level.OFF);
    }

    private static final Logger LOGGER = Logger.getLogger(Filterer.class.getName());

    /**
     * Filters applications by the given status string with enhanced validation.
     *
     * @param applications The master list of applications.
     * @param statusQuery  The status to search for (e.g., "OFFER").
     * @throws JobPilotException if input is invalid
     */
    public static ArrayList<Application> filterByStatus(ArrayList<Application> applications, String statusQuery) throws JobPilotException {

        assert applications != null : "The applications list should not be null";

        if (statusQuery == null || statusQuery.isBlank()) {
            throw new JobPilotException("Status filter cannot be empty. Use: filter s/OFFER");
        }

        String normalizedQuery = statusQuery.trim().toUpperCase();

        if (normalizedQuery.isEmpty()) {
            throw new JobPilotException("Status keyword cannot be empty!");
        }

        LOGGER.log(Level.INFO, "Filtering applications for status: " + normalizedQuery);

        ArrayList<Application> filteredResults = new ArrayList<>();

        for (Application app : applications) {
            if (app != null && app.getStatus() != null) {
                if (app.getStatus().toUpperCase().contains(normalizedQuery)) {
                    filteredResults.add(app);
                }
            }
        }

        if (filteredResults.isEmpty()) {
            LOGGER.log(Level.WARNING, "No matches found for status: " + normalizedQuery);
        }

        showFilterResults(filteredResults, normalizedQuery);
        return filteredResults;
    }

    /**
     * Displays formatted filter results with a summary header.
     */
    private static void showFilterResults(ArrayList<Application> results, String status) {
        Ui.showFilterResults(results, status);
    }
}
//@@author

