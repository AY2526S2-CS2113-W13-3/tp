package ui;

import exception.JobPilotException;
import parser.Parser;
import parser.ParsedCommand;
import task.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for the JobPilot application.
 * Handles the user interface and coordinates application logic.
 */
public class JobPilot {
    private static final Logger LOGGER = Logger.getLogger(JobPilot.class.getName());

    public static void main(String[] args) {
        LOGGER.setLevel(Level.OFF);

        Ui.showWelcome();
        ArrayList<Application> applications = new ArrayList<>();

        while (true) {
            String input = Ui.readCommand();
            ParsedCommand cmd = Parser.parse(input);

            switch (cmd.type) {
                case BYE:
                    Ui.showGoodbye(applications.size());
                    Ui.close();
                    return;

                case HELP:
                    Ui.showHelp();
                    break;

                case ADD:
                    try {
                        Application newApp = new Application(cmd.company, cmd.position, cmd.date);
                        applications.add(newApp);
                        Ui.showApplicationAdded(newApp);
                    } catch (DateTimeParseException e) {
                        Ui.showError("Invalid date! Please use YYYY-MM-DD (e.g., 2024-09-12)");
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

                case UNKNOWN:
                default:
                    Ui.showError("Unknown command. Use 'help' to see all available commands!");
                    break;
            }
        }
    }
}