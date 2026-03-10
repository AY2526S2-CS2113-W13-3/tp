package seedu.duke;

import task.Add;
import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    /**
     * Main entry-point for the java.duke.Duke application.
     */

    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);

        System.out.println("Welcome to Job Application Tracker!");
        System.out.println("Commands: add | bye");
        System.out.println("Format: add c/COMPANY p/POSITION d/DATE");
        System.out.println("Example: add c/Google p/Software Engineer Intern d/2024-09-12");

        Scanner in = new Scanner(System.in);
        ArrayList<Add> applications = new ArrayList<>();

        while (true) {
            System.out.print("> ");

            if (!in.hasNextLine()) {
                break;
            }
            String input = in.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("Bye! You added " + applications.size() + " application(s).");
                break;
            } else if (input.startsWith("add ")) {
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

                System.out.println("Added: " + company + " | " + position + " | " + date + " | Pending");
                System.out.println("You have " + applications.size() + " application(s)");

            } else {
                System.out.println("Unknown command. Use: add or bye");
            }
        }

        in.close();
    }
}


