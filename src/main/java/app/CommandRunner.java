package app;

import exception.JobPilotException;
import parser.ParsedCommand;
import task.Application;
import task.Deleter;
import task.Editor;
import task.Filterer;
import ui.Ui;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Executes parsed commands and applies them to the application list.
 */
public class CommandRunner {

    private final ArrayList<Application> applications;

    /**
     * Initializes the command runner with the application list.
     */
    public CommandRunner(ArrayList<Application> applications) {
        this.applications = applications;
    }

    /**
     * Runs a parsed command.
     *
     * @return true to continue, false to exit the program
     */
    public boolean run(ParsedCommand cmd) {
        // Updated to use Getter: getType()
        switch (cmd.getType()) {

        case BYE:
            Ui.showGoodbye(applications.size());
            Ui.close();
            return false;

        case HELP:
            Ui.showHelp();
            break;

        case ADD:
            try {
                // Updated to use Getters: getCompany(), getPosition(), getDate()
                Application newApp = new Application(cmd.getCompany(), cmd.getPosition(), cmd.getDate());
                applications.add(newApp);
                Ui.showApplicationAdded(newApp);
            } catch (DateTimeParseException e) {
                Ui.showError("Invalid date! Please use YYYY-MM-DD");
            }
            break;

        case LIST:
            Ui.showApplicationList(applications);
            break;

        case DELETE:
            try {
                // Updated to use Getter: getIndex()
                Application removed = Deleter.deleteApplication(applications, cmd.getIndex());
                Ui.showApplicationDeleted(removed, applications.size());
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case EDIT:
            try {
                // Updated to use Getters for Edit fields
                Editor.editApplication(cmd.getIndex(), applications,
                        cmd.getNewCompany(), cmd.getNewPosition(), cmd.getNewDate(), cmd.getNewStatus());
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case FILTER:
            // Updated to use Getter: getSearchTerm()
            Filterer.filterByStatus(applications, cmd.getSearchTerm(), null);
            break;

        case SORT:
            Collections.sort(applications);
            Ui.showSortedMessage();
            Ui.showApplicationList(applications);
            break;

        case SEARCH:
            handleSearch(cmd.getSearchTerm());
            break;

        case STATUS:
            handleStatusUpdate(cmd);
            break;

        case TAG:
            handleTagUpdate(cmd);
            break;

        case ERROR:
            // Updated to use Getter: getErrorMessage()
            Ui.showError(cmd.getErrorMessage());
            break;

        default:
            Ui.showError("Unknown command. Type 'help' to see all available commands.");
        }

        return true;
    }

    /**
     * Handles Status and Notes updates with defensive null checks.
     */
    private void handleStatusUpdate(ParsedCommand cmd) {
        int idx = cmd.getIndex();
        if (idx < 0 || idx >= applications.size()) {
            Ui.showError("Invalid index! Application not found.");
            return;
        }

        Application app = applications.get(idx);

        // Defensive check: Only update if the specific field was provided in the command
        if (cmd.getStatusValue() != null) {
            app.setStatus(cmd.getStatusValue());
        }
        if (cmd.getNote() != null) {
            app.setNotes(cmd.getNote());
        }

        Ui.showStatusUpdated(app);
    }

    /**
     * Handles searching with a safety check for empty lists.
     */
    private void handleSearch(String query) {
        if (applications.isEmpty()) {
            Ui.showError("No applications to search!");
            return;
        }

        ArrayList<Application> results = new ArrayList<>();
        for (Application app : applications) {
            if (app.getCompany().toLowerCase().contains(query.toLowerCase())) {
                results.add(app);
            }
        }
        Ui.showSearchResults(results, query);
    }

    /**
     * Handles Tag additions and removals.
     */
    private void handleTagUpdate(ParsedCommand cmd) {
        int idx = cmd.getIndex();
        if (idx < 0 || idx >= applications.size()) {
            Ui.showError("Invalid index! Application not found.");
            return;
        }

        Application target = applications.get(idx);
        if (cmd.isAddTag()) {
            target.addIndustryTag(cmd.getTag());
            Ui.showTagAdded(cmd.getTag(), target);
        } else {
            target.removeIndustryTag(cmd.getTag());
            Ui.showTagRemoved(cmd.getTag(), target);
        }
    }
}