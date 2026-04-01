package app;

import exception.JobPilotException;
import parser.Parser;
import task.Application;
import task.Editor;
import task.Filterer;
import ui.Ui;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Executes parsed commands and manages application flow.
 */
public class CommandRunner {

    private final ArrayList<Application> applications;

    public CommandRunner(ArrayList<Application> applications) {
        this.applications = applications;
    }

    public boolean run(Parser.Command cmd) {
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
                    Ui.showError("Invalid date format! Use: yyyy-MM-dd");
                }
                break;

            case LIST:
                Ui.showApplicationList(applications);
                break;

            case DELETE:
                try {
                    Application removed = Editor.deleteApplication(applications, cmd.index);
                    Ui.showApplicationDeleted(removed, applications.size());
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                }
                break;

            case EDIT:
                try {
                    Editor.editApplication(applications, cmd.index, cmd.newCompany,
                            cmd.newPosition, cmd.newDate, cmd.newStatus);
                    Ui.showApplicationEdited(applications.get(cmd.index - 1));
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                }
                break;

            case FILTER:
                try {
                    Filterer.filterByStatus(applications, cmd.searchTerm, Ui.getInstance());
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                }
                break;

            case SORT:
                if (applications.isEmpty()) {
                    Ui.showError("No applications to sort!");
                    break;
                }

                String sortType = cmd.searchTerm != null ? cmd.searchTerm.trim().toLowerCase() : "";
                boolean reverse = sortType.contains("reverse");

                if (sortType.isEmpty() || sortType.startsWith("date")) {
                    if (reverse) {
                        applications.sort(Collections.reverseOrder());
                    } else {
                        Collections.sort(applications);
                    }
                } else if (sortType.startsWith("company")) {
                    if (reverse) {
                        applications.sort((a, b) -> b.getCompany().compareTo(a.getCompany()));
                    } else {
                        applications.sort((a, b) -> a.getCompany().compareTo(b.getCompany()));
                    }
                } else if (sortType.startsWith("status")) {
                    if (reverse) {
                        applications.sort((a, b) -> b.getStatus().compareTo(a.getStatus()));
                    } else {
                        applications.sort((a, b) -> a.getStatus().compareTo(b.getStatus()));
                    }
                } else {
                    Ui.showError("Invalid sort type! Use: sort date/company/status [reverse]");
                    break;
                }

                Ui.showSortedMessage();
                Ui.showApplicationList(applications);
                break;

            case SEARCH:
                if (applications.isEmpty()) {
                    Ui.showError("No applications to search!");
                    break;
                }

                String rawSearchTerm = cmd.searchTerm.trim();
                if (rawSearchTerm.isEmpty()) {
                    Ui.showError("Please enter a valid search term.");
                    break;
                }

                boolean isExactMatch = rawSearchTerm.startsWith("exact:");
                boolean isNegativeSearch = rawSearchTerm.startsWith("!");
                String[] searchKeywords;

                if (isExactMatch) {
                    searchKeywords = new String[]{rawSearchTerm.substring("exact:".length()).trim().toLowerCase()};
                } else if (isNegativeSearch) {
                    searchKeywords = new String[]{rawSearchTerm.substring(1).trim().toLowerCase()};
                } else {
                    searchKeywords = rawSearchTerm.split("\\s+");
                    for (int i = 0; i < searchKeywords.length; i++) {
                        searchKeywords[i] = searchKeywords[i].trim().toLowerCase();
                    }
                }

                ArrayList<Application> results = new ArrayList<>();
                for (Application app : applications) {
                    String companyLower = app.getCompany().toLowerCase();
                    boolean matches = true;

                    for (String keyword : searchKeywords) {
                        if (keyword.isEmpty()) continue;

                        if (isExactMatch) {
                            if (!companyLower.equals(keyword)) {
                                matches = false;
                                break;
                            }
                        } else if (isNegativeSearch) {
                            if (companyLower.contains(keyword)) {
                                matches = false;
                                break;
                            }
                        } else {
                            if (!companyLower.contains(keyword)) {
                                matches = false;
                                break;
                            }
                        }
                    }

                    if (matches) {
                        results.add(app);
                    }
                }

                Collections.sort(results);
                Ui.showSearchResults(results, rawSearchTerm);
                break;

            case STATUS:
                try {
                    Editor.updateStatus(applications, cmd.index, cmd.status, cmd.note);
                    Ui.showStatusUpdated(applications.get(cmd.index - 1));
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                }
                break;

            case TAG:
                // Handled in Parser + Editor, no logic here
                break;

            case ERROR:
                Ui.showError(cmd.errorMessage);
                break;

            default:
                Ui.showError("Unknown command. Type 'help' to see available commands.");
        }

        return true;
    }
}