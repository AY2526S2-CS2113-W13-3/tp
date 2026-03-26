package app;

import exception.JobPilotException;
import parser.ParsedCommand;
import task.*;
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
        switch (cmd.type) {

        case BYE:
            Ui.showGoodbye(applications.size());
            Ui.close();
            return false;

        case HELP:
            Ui.showHelp();
            break;

        case ADD:
            try {
                Application newApp = new Application(cmd.company, cmd.position, cmd.date);
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
                Application removed = Deleter.deleteApplication(applications, cmd.index);
                Ui.showApplicationDeleted(removed, applications.size());
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case EDIT:
            try {
                Editor.editApplication(cmd.index, applications,
                        cmd.newCompany, cmd.newPosition, cmd.newDate, cmd.newStatus);
            } catch (JobPilotException e) {
                Ui.showError(e.getMessage());
            }
            break;

        case SORT:
            Collections.sort(applications);
            Ui.showSortedMessage();
            Ui.showApplicationList(applications);
            break;

        case SEARCH:
            if (applications.isEmpty()) {
                Ui.showError("No applications to search!");
                break;
            }

            ArrayList<Application> results = new ArrayList<>();
            for (Application app : applications) {
                if (app.getCompany().toLowerCase().contains(cmd.searchTerm.toLowerCase())) {
                    results.add(app);
                }
            }
            Ui.showSearchResults(results, cmd.searchTerm);
            break;

        case STATUS:
            if (cmd.index < 0 || cmd.index >= applications.size()) {
                Ui.showError("Invalid index! Application not found.");
                break;
            }
            Application app = applications.get(cmd.index);
            app.setStatus(cmd.statusValue);
            app.setNotes(cmd.note);
            Ui.showStatusUpdated(app);
            break;

        case TAG:
            if (cmd.index < 0 || cmd.index >= applications.size()) {
                Ui.showError("Invalid index! Application not found.");
                break;
            }
            Application target = applications.get(cmd.index);
            if (cmd.isAddTag) {
                target.addIndustryTag(cmd.tag);
                Ui.showTagAdded(cmd.tag, target);
            } else {
                target.removeIndustryTag(cmd.tag);
                Ui.showTagRemoved(cmd.tag, target);
            }
            break;

        case ERROR:
            Ui.showError(cmd.errorMessage);
            break;

        default:
            Ui.showError("Unknown command.");
        }

        return true;
    }
}
