package seedu.duke;

import task.Add;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    /**
     * Method for listing all the applications
     *
     * @param applications
     */
    private static void listApplications(ArrayList<Add> applications) {
        if (applications.isEmpty()) {
            System.out.println("There is no application yet.");
        } else {
            System.out.println("Here are your applications:");
            int index = 0;
            for (Add app : applications) {
                System.out.println((index + 1) + ". " + applications.get(index));
                index++;
            }
        }
    }

    private static void sortApplications(ArrayList<Add> applications) {
        if (applications.isEmpty()) {
            System.out.println("No applications to sort!");
            return;
        }
        Collections.sort(applications);
        System.out.println("Sorted by submission date!");
        listApplications(applications);
    }
    /**
     * Main entry-point for the java.duke.Duke application.
     */
    public static void main(String[] args) {
        String logo = ""
                + "     _   ___   ____   ____   ___  _       ___   _____ \n"
                + "    | | / _ \\ | __ ) |  _ \\ |_ _|| |     / _ \\ |_   _|\n"
                + " _  | || | | ||  _ \\ | |_) | | | | |    | | | |  | |  \n"
                + "| |_| || |_| || |_) ||  __/  | | | |___ | |_| |  | |  \n"
                + " \\___/  \\___/ |____/ |_|    |___||_____| \\___/   |_|  \n";
        System.out.println("Hello from\n" + logo);

        System.out.println("Welcome to Job Application Tracker!");
        System.out.println("Commands: add | list | sort | bye");
        System.out.println("Format: add c/COMPANY p/POSITION d/DATE");
        System.out.println("Example: add c/Google p/Software Engineer Intern d/2024-09-12");

        Scanner in = new Scanner(System.in);
        ArrayList<Add> applications = new ArrayList<>();

        while (true) {
            System.out.print("> ");
            String input = in.nextLine().trim();

            // ADDED: Handle commands
            if (input.equals("bye")) {
                System.out.println("Bye! You added " + applications.size() + " application(s).");
                break;
            } else if (input.startsWith("add ")) {
                // Parse the add command
                int cIndex = input.indexOf("c/");
                int pIndex = input.indexOf("p/");
                int dIndex = input.indexOf("d/");

                if (cIndex == -1 || pIndex == -1 || dIndex == -1) {
                    System.out.println("Invalid format! Use: add c/COMPANY p/POSITION d/DATE");
                    continue;
                }

                String company = input.substring(cIndex + 2, pIndex).trim();
                String position = input.substring(pIndex + 2, dIndex).trim();
                String date = input.substring(dIndex + 2).trim();

                if (company.isEmpty() || position.isEmpty() || date.isEmpty()) {
                    System.out.println("Fields cannot be empty!");
                    continue;
                }

                Add app = new Add(company, position, date);
                applications.add(app);

                System.out.println("Added: " + app);
                System.out.println("You have " + applications.size() + " application(s)");

            } else if (input.equals("list")) {
                listApplications(applications);
            } else if (input.equals("sort")){
                sortApplications(applications);
            } else {
                System.out.println("Unknown command. Use: add or list or sort or bye");
            }
        }

        in.close();
    }
}


