package app;

import exception.JobPilotException;
import parser.ParsedCommand;
import parser.CommandType;
import task.Application;
import task.Editor;
import task.Filterer;
import task.Deleter;
import task.IndustryTag;
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

    public boolean run(ParsedCommand cmd) {
        if (cmd == null) {
            Ui.showError("Invalid command!");
            return true;
        }

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
                    Application removed = Deleter.deleteApplication(applications, cmd.index);
                    Ui.showApplicationDeleted(removed, applications.size());
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                }
                break;

            case EDIT:
                try {
                    Editor.editApplication(cmd.index, applications,
                            cmd.newCompany, cmd.newPosition,
                            cmd.newDate, cmd.newStatus);
                    Ui.showApplicationEdited(applications.get(cmd.index));
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                }
                break;

            case FILTER:
                try {
                    Filterer.filterByStatus(applications, cmd.searchTerm);
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

                String rawSearchTerm = cmd.searchTerm != null ? cmd.searchTerm.trim() : "";
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
                    int index = cmd.index;
                    String newStatus = cmd.statusValue;
                    String note = cmd.note;

                    if (index < 0 || index >= applications.size()) {
                        Ui.showError("Invalid application number! You have " + applications.size() + " application(s).");
                        break;
                    }

                    Application app = applications.get(index);

                    if (newStatus != null && !newStatus.isEmpty()) {
                        app.setStatus(newStatus);
                    }
                    if (note != null) {
                        app.setNotes(note);
                    }

                    Ui.showStatusUpdated(app);
                } catch (Exception e) {
                    Ui.showError("Invalid status command! Use: status INDEX set/STATUS note/NOTE");
                }
                break;

            case TAG:
                try {
                    int index = cmd.index;
                    IndustryTag tag = cmd.tag;
                    boolean isAdd = cmd.isAddTag;

                    if (index < 0 || index >= applications.size()) {
                        Ui.showError("Invalid application number! You have " + applications.size() + " application(s).");
                        break;
                    }

                    Application app = applications.get(index);

                    if (isAdd) {
                        app.addIndustryTag(tag);
                        Ui.showTagAdded(tag, app);
                    } else {
                        app.removeIndustryTag(tag);
                        Ui.showTagRemoved(tag, app);
                    }
                } catch (AssertionError e) {
                    Ui.showError(e.getMessage());
                } catch (Exception e) {
                    Ui.showError("Invalid tag command! Use: tag INDEX add/TAG or tag INDEX remove/TAG");
                }
                break;

            case UNKNOWN:
                Ui.showError("Unknown command. Type 'help' to see available commands.");
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