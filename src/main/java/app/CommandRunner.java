package app;

import exception.JobPilotException;
import parser.ParsedCommand;
import parser.CommandType;
import task.Application;
import task.Deleter;
import task.Editor;
import task.Filterer;
import ui.Ui;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
            return true;
        }

        switch (cmd.getType()) {
        case BYE:
            return handleBye();

        case HELP:
            handleHelp();
            break;

        case ADD:
            handleAdd(cmd);
            break;

        case LIST:
            handleList();
            break;

        case DELETE:
            handleDelete(cmd);
            break;

        case EDIT:
            handleEdit(cmd);
            break;

        case FILTER:
            handleFilter(cmd);
            break;

        case SORT:
            handleSort(cmd.getSearchTerm());
            break;

        case SEARCH:
            handleSearch(cmd.getSearchType(), cmd.getSearchTerm());
            break;

        case STATUS:
            handleStatusUpdate(cmd);
            break;

        case TAG:
            handleTagUpdate(cmd);
            break;

        case ERROR:
            Ui.showError(cmd.getErrorMessage());
            break;

        default:
            Ui.showError("Unknown command. Type 'help' to see all available commands.");
        }

        return true;
    }

    // ===================== BASIC COMMANDS =====================

    private boolean handleBye() {
        try {
            Ui.showGoodbye(applications.size());
            Ui.close();
        } catch (IllegalStateException e) {
            Ui.showError("Scanner was already closed.");
        }
        return false;
    }

    private void handleHelp() {
        Ui.showHelp();
    }

    private void handleAdd(ParsedCommand cmd) {
        try {
            Application newApp = new Application(
                    cmd.getCompany(), cmd.getPosition(), cmd.getDate());
            applications.add(newApp);
            Ui.showApplicationAdded(newApp);
        } catch (DateTimeParseException e) {
            Ui.showError("Invalid date! Please use YYYY-MM-DD");
        }
    }

    private void handleList() {
        Ui.showApplicationList(applications);
    }

    private void handleDelete(ParsedCommand cmd) {
        try {
            Application removed = Deleter.deleteApplication(applications, cmd.getIndex());
            Ui.showApplicationDeleted(removed, applications.size());
        } catch (JobPilotException e) {
            Ui.showError(e.getMessage());
        }
    }

    private void handleEdit(ParsedCommand cmd) {
        try {
            Editor.editApplication(cmd.getIndex(), applications,
                    cmd.getNewCompany(), cmd.getNewPosition(),
                    cmd.getNewDate(), cmd.getNewStatus());
            Ui.showApplicationEdited(applications.get(cmd.getIndex()));
        } catch (JobPilotException e) {
            Ui.showError(e.getMessage());
        }
    }

    private void handleFilter(ParsedCommand cmd) {
        try {
            Filterer.filterByStatus(applications, cmd.getSearchTerm());
        } catch (JobPilotException e) {
            Ui.showError(e.getMessage());
        }
    }

    // ===================== SORT =====================

    private void handleSort(String rawSortTerm) {
        if (applications.isEmpty()) {
            Ui.showError("There is no application yet.");
            return;
        }

        String sortType = rawSortTerm != null ? rawSortTerm.trim().toLowerCase() : "";
        boolean reverse = sortType.contains("reverse");

        Comparator<Application> comparator = getComparator(sortType);

        if (reverse) {
            applications.sort(Collections.reverseOrder(comparator));
        } else {
            applications.sort(comparator);
        }

        Ui.showSortedMessage();
        Ui.showApplicationList(applications);
    }

    private Comparator<Application> getComparator(String sortType) {
        if (sortType.startsWith("company")) {
            return Comparator.comparing(a -> a.getCompany().toLowerCase());
        } else if (sortType.startsWith("status")) {
            return Comparator.comparing(a -> a.getStatus().toLowerCase());
        } else {
            return Comparator.naturalOrder();
        }
    }

    // ===================== SEARCH =====================

    private void handleSearch(String type, String query) {
        if (applications.isEmpty()) {
            Ui.showError("Application list is empty!");
            return;
        }

        ArrayList<Application> results = findMatches(type, query);

        String label = (type != null ? type : "c") + "/" + query;
        Ui.showSearchResults(results, label);
    }

    private ArrayList<Application> findMatches(String type, String query) {
        ArrayList<Application> results = new ArrayList<>();
        String lowerQuery = query != null ? query.toLowerCase() : "";

        for (Application app : applications) {
            if (matches(app, type, lowerQuery)) {
                results.add(app);
            }
        }

        Collections.sort(results);
        return results;
    }

    private boolean matches(Application app, String type, String query) {
        String searchType = (type != null) ? type : "c";

        switch (searchType) {
        case "c":
            return app.getCompany().toLowerCase().contains(query);
        case "p":
            return app.getPosition().toLowerCase().contains(query);
        case "s":
            return app.getStatus().toLowerCase().contains(query);
        default:
            return false;
        }
    }

    // ===================== STATUS =====================

    private void handleStatusUpdate(ParsedCommand cmd) {
        int idx = cmd.getIndex();
        if (!isValidIndex(idx)) {
            Ui.showError("Invalid index! Application not found.");
            return;
        }

        Application app = applications.get(idx);

        if (cmd.getStatusValue() != null) {
            app.setStatus(cmd.getStatusValue());
        }
        if (cmd.getNote() != null) {
            app.setNotes(cmd.getNote());
        }

        Ui.showStatusUpdated(app);
    }

    // ===================== TAG =====================

    private void handleTagUpdate(ParsedCommand cmd) {
        int idx = cmd.getIndex();
        if (!isValidIndex(idx)) {
            Ui.showError("Invalid index! Application not found.");
            return;
        }

        Application target = applications.get(idx);

        try {
            if (cmd.isAddTag()) {
                target.addIndustryTag(cmd.getTag());
                Ui.showTagAdded(cmd.getTag(), target);
            } else {
                target.removeIndustryTag(cmd.getTag());
                Ui.showTagRemoved(cmd.getTag(), target);
            }
        } catch (Exception e) {
            Ui.showError(e.getMessage());
        }
    }

    // ===================== HELPER =====================

    private boolean isValidIndex(int idx) {
        return idx >= 0 && idx < applications.size();
    }
}