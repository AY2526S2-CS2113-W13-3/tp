package jobpilot;

import exception.JobPilotException;
import task.Add;
import task.Delete;

import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;

public class JobPilot {

    /**
     * Adds a new job application to the list.
     *
     * @param applications The list of job applications
     * @param input The raw user command string
     * @throws JobPilotException If there's an error in the command format
     */
    public static void addApplication(ArrayList<Add> applications, String input) throws JobPilotException {
        try {
            int cIndex = input.indexOf("c/");
            int pIndex = input.indexOf("p/");
            int dIndex = input.indexOf("d/");

            if (cIndex == -1 || pIndex == -1 || dIndex == -1) {
                throw new JobPilotException("Missing required fields! Use: add c/COMPANY p/POSITION d/DATE");
            }

            if (cIndex > pIndex || pIndex > dIndex) {
                throw new JobPilotException("Wrong order! Use: c/COMPANY then p/POSITION then d/DATE");
            }

            String company = input.substring(cIndex + 2, pIndex).trim();
            String position = input.substring(pIndex + 2, dIndex).trim();
            String dateStr = input.substring(dIndex + 2).trim();

            if (company.isEmpty() || position.isEmpty() || dateStr.isEmpty()) {
                throw new JobPilotException("Fields cannot be empty!");
            }

            Add app = new Add(company, position, dateStr);
            applications.add(app);

            System.out.println("Added: " + app);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date! Please use YYYY-MM-DD (e.g., 2024-09-12)");
        } catch (StringIndexOutOfBoundsException e) {
            throw new JobPilotException("Error parsing command!");
        }
    }

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
     * Updates the status and note of an existing application.
     *
     * @param applications The list containing job applications.
     * @param input The raw user command string.
     */
    public static void updateStatus(ArrayList<Add> applications, String input) {
        try {
            // Format: status INDEX set/STATUS note/NOTE
            int setIndex = input.indexOf("set/");
            int noteIndex = input.indexOf("note/");

            if (setIndex == -1 || noteIndex == -1) {
                System.out.println("Invalid format! Use: status INDEX set/STATUS note/NOTE");
                return;
            }

            // Extract the index (between 'status ' and ' set/')
            String indexStr = input.substring("status ".length(), setIndex).trim();
            int listIndex = Integer.parseInt(indexStr) - 1;

            if (listIndex < 0 || listIndex >= applications.size()) {
                System.out.println("Invalid index! Application not found.");
                return;
            }

            String newStatus = input.substring(setIndex + 4, noteIndex).trim().toUpperCase();
            String note = input.substring(noteIndex + 5).trim();

            // Update the application
            Add app = applications.get(listIndex);
            app.setStatus(newStatus + " (Note: " + note + ")");

            System.out.println("Updated Status: " + app);
        } catch (Exception e) {
            System.out.println("Error parsing status command. Ensure index is a number.");
        }
    }


    /**
     * Main entry-point for the application.
     */
    public static void main(String[] args) {
        String logo = ""
                + "     _   ___   ____   ____   ___  _       ___   _____ \n"
                + "    | | / _ \\ | __ ) |  _ \\ |_ _|| |     / _ \\ |_   _|\n"
                + " _  | || | | ||  _ \\ | |_) | | | | |    | | | |  | |  \n"
                + "| |_| || |_| || |_) ||  __/  | | | |___ | |_| |  | |  \n"
                + " \\___/  \\___/ |____/ |_|    |___||_____| \\___/   |_|  \n";
        System.out.println("Hello from\n" + logo);

        System.out.println("Welcome to JobPilot!");
        System.out.println("Commands: add | list | sort | status | delete | bye");

        Scanner in = new Scanner(System.in);
        ArrayList<Add> applications = new ArrayList<>();

        while (true) {
            System.out.print("> ");
            String input = in.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("Bye! You added " + applications.size() + " application(s).");
                break;
            } else if (input.startsWith("add")) {
                try {
                    addApplication(applications, input);
                } catch (JobPilotException e) {
                    System.out.println(e.getMessage());
                }
            } else if (input.equals("list")) {
                listApplications(applications);
            } else if (input.equals("sort")) {
                sortApplications(applications);
            } else if (input.startsWith("status ")) {
                updateStatus(applications, input);
            } else if (input.startsWith("delete")) {
                try {
                    deleteApplication(input, applications);
                } catch (JobPilotException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Unknown command. Use: add or list or sort or status or delete or bye");
            }
        }

        in.close();
    }

    /**
     * Deletes an application from the list by parsing the index provided by the user.
     *
     * @param input The full user command (e.g., "delete 2").
     * @param applications The list storing all job applications.
     * @throws JobPilotException If the index provided is not a valid integer.
     */
    private static void deleteApplication(String input, ArrayList<Add> applications) throws JobPilotException {
        try {
            Delete.deleteApplication(input, applications);
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid format! Use: delete INDEX");
        }
    }
}



