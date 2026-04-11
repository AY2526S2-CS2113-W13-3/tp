package ui;

import task.Application;
import task.IndustryTag;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all user interactions, including reading input from the console
 * and displaying formatted messages, lists, and errors to the user.
 */
public class Ui {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String HORIZONTAL_LINE =
            "___________________________________________________________________";

    /**
     * Reads a command entered by the user from the console.
     *
     * @return The trimmed user input string.
     */
    public static String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays the welcome message and application logo.
     */
    public static void showWelcome() {
        String logo = """
                     _   ___   ____   ____   ___  _       ___   _____
                    | | / _ \\ | __ ) |  _ \\ |_ _|| |     / _ \\ |_   _|
                 _  | || | | ||  _ \\ | |_) | | | | |    | | | |  | |
                | |_| || |_| || |_) ||  __/  | | | |___ | |_| |  | |
                 \\___/  \\___/ |____/ |_|    |___||_____| \\___/   |_|
                """;
        System.out.println("Hello from\n" + logo);
        System.out.println("Welcome to JobPilot!");
        System.out.println("Type 'help' to see all available commands!");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a goodbye message along with the total number of applications added.
     *
     * @param count The number of applications added during the session.
     */
    public static void showGoodbye(int count) {
        System.out.println("Bye! You added " + count + " application(s).");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays an error message to the user.
     *
     * @param error The error message to be displayed.
     */
    public static void showError(String error) {
        System.out.println(error);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a message indicating that an application has been added.
     *
     * @param app The application that was added.
     */
    public static void showApplicationAdded(Application app) {
        System.out.println("Added: " + app);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a message indicating that an application has been deleted.
     *
     * @param app           The application that was deleted.
     * @param remainingSize The number of applications remaining.
     */
    public static void showApplicationDeleted(Application app, int remainingSize) {
        System.out.println("Deleted application:");
        System.out.println(app);
        System.out.println("You have " + remainingSize + " application(s) left.");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a message indicating that an application has been edited.
     *
     * @param app The updated application.
     */
    public static void showApplicationEdited(Application app) {
        System.out.println("Updated application:");
        System.out.println(app);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a list of applications.
     *
     * @param applications The list of applications to display.
     */
    public static void showApplicationList(ArrayList<Application> applications) {
        if (applications.isEmpty()) {
            System.out.println("There is no application yet.");
            System.out.println(HORIZONTAL_LINE);
            return;
        }
        System.out.println("Here are your applications:");
        for (int i = 0; i < applications.size(); i++) {
            if (applications.get(i) == null) {
                System.out.println((i + 1) + ". [Invalid application data]");
            } else {
                System.out.println((i + 1) + ". " + applications.get(i));
            }
        }
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays search results for applications based on a search term.
     *
     * @param results    The list of matching applications.
     * @param searchTerm The company name or keyword used for the search.
     */
    public static void showSearchResults(ArrayList<Application> results, String searchTerm) {
        if (results.isEmpty()) {
            System.out.println("No applications found matching '" + searchTerm + "'.");
        } else {
            System.out.println("Found " + results.size() + " application(s) matching '" + searchTerm + "':");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays the results of a status filter operation.
     * If no results are found, it informs the user. Otherwise, it prints
     * the count and delegates to showApplicationList to display the details.
     *
     * @param results The list of applications matching the filter criteria.
     * @param status  The status string used for the filter.
     */
    public static void showFilterResults(ArrayList<Application> results, String status) {
        System.out.println("Filtered by status: " + status);

        if (results.isEmpty()) {
            System.out.println("No applications found with status: " + status);
            System.out.println(HORIZONTAL_LINE);
        } else {
            System.out.println("Found " + results.size() + " application(s) with status '" + status + "':");
            showApplicationList(results);
        }
    }

    /**
     * Displays a message indicating that applications have been sorted.
     */
    public static void showSortedMessage() {
        System.out.println("Sorted by submission date!");
    }

    /**
     * Displays a message indicating that an application's status has been updated.
     *
     * @param app The application with the updated status.
     */
    public static void showStatusUpdated(Application app) {
        System.out.println("Updated Status: " + app);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a message indicating that a tag has been added to an application.
     *
     * @param tag The tag that was added.
     * @param app The application to which the tag was added.
     */
    public static void showTagAdded(IndustryTag tag, Application app) {
        System.out.println("Added tag: " + tag + " -> " + app);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays a message indicating that a tag has been removed from an application.
     *
     * @param tag The tag that was removed.
     * @param app The application from which the tag was removed.
     */
    public static void showTagRemoved(IndustryTag tag, Application app) {
        System.out.println("Removed tag: " + tag + " -> " + app);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Displays the help message to the user with all available commands.
     */
    public static void showHelp() {
        String helpMessage = """ 
                Available Commands:
                add c/COMPANY p/POSITION d/DATE                             Add a new job application
                edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]     Edit existing application
                delete INDEX                                                Delete an application
                status INDEX [s/STATUS] [note/NOTE]                         Update application status and add a note
                filter s/STATUS                                             Filter applications by status
                tag INDEX add/TAG                                           Add a tag to an application
                tag INDEX remove/TAG                                        Remove a tag from an application
                list                                                        List all job applications
                sort                                                        Sort applications by date
                search [c/COMPANY] / [p/POSITION] / [s/STATUS]              Search applications
                help                                                        Show this message
                bye                                                         Exit the application""";

        System.out.println(helpMessage);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Closes the scanner used for reading user input.
     */
    public static void close() {
        scanner.close();
    }
}
