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

    public static String readCommand() {
        return scanner.nextLine().trim();
    }

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
    }

    public static void showGoodbye(int count) {
        System.out.println("Bye! You added " + count + " application(s).");
    }

    public static void showMessage(String message) {
        System.out.println(message);
    }

    public static void showError(String error) {
        System.out.println(error);
    }

    public static void showApplicationAdded(Application app) {
        System.out.println("Added: " + app);
    }

    public static void showApplicationDeleted(Application app, int remainingSize) {
        System.out.println("Deleted application:");
        System.out.println(app);
        System.out.println("You have " + remainingSize + " application(s) left.");
    }

    public static void showApplicationEdited(Application app) {
        System.out.println("Updated application:");
        System.out.println(app);
    }

    public static void showApplicationList(ArrayList<Application> applications) {
        if (applications.isEmpty()) {
            System.out.println("There is no application yet.");
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
    }

    public static void showSearchResults(ArrayList<Application> results, String searchTerm) {
        if (results.isEmpty()) {
            System.out.println("No applications found for company: " + searchTerm);
        } else {
            System.out.println("Found " + results.size() + " application(s) matching '" + searchTerm + "':");
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }

    public static void showSortedMessage() {
        System.out.println("Sorted by submission date!");
    }

    public static void showStatusUpdated(Application app) {
        System.out.println("Updated Status: " + app);
    }

    public static void showTagAdded(IndustryTag tag, Application app) {
        System.out.println("Added tag: " + tag + " -> " + app);
    }

    public static void showTagRemoved(IndustryTag tag, Application app) {
        System.out.println("Removed tag: " + tag + " -> " + app);
    }

    public static void showHelp() {
        String helpMessage = """ 
                Available Commands:
                add c/COMPANY p/POSITION d/DATE                             Add a new job application
                edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]     Edit existing application
                list                                                        List all job applications
                delete INDEX                                                Delete an application
                sort                                                        Sort applications by date
                search COMPANY_NAME                                         Search applications by company name
                status INDEX set/STATUS note/NOTE                           Update application status and add a note
                tag INDEX add/TAG                                           Add a tag to an application
                tag INDEX remove/TAG                                        Remove a tag from an application
                help                                                        Show this message
                bye                                                         Exit the application
                """;
        System.out.println(helpMessage);
    }

    public static void close() {
        scanner.close();
    }
}
