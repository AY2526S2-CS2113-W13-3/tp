package task;

import exception.JobPilotException;
import ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Handles editing existing job applications.
 */
public class Editor {

    /**
     * Wrapper method that parses raw command string and calls the main edit method.
     * Format: edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]
     *
     * @param input The raw user command (e.g., "edit 1 c/Google")
     * @param applications The list storing all job applications
     * @throws JobPilotException If parsing fails or index is invalid
     */
    public static void editApplication(String input, ArrayList<Application> applications)
            throws JobPilotException {
        try {
            String normalized = input.trim().replaceAll("\\s+", " ");
            String[] parts = normalized.split(" ");

            if (parts.length < 2) {
                throw new JobPilotException("Please provide an index. Example: edit 1 c/Google");
            }

            int index = Integer.parseInt(parts[1]) - 1;

            int fieldsStart = normalized.indexOf(" ", normalized.indexOf(" ") + 1) + 1;
            if (fieldsStart <= 0 || fieldsStart >= normalized.length()) {
                throw new JobPilotException("No valid fields to update!");
            }

            String remaining = normalized.substring(fieldsStart);

            String company = null;
            String position = null;
            String date = null;
            String status = null;

            int pos = 0;
            while (pos < remaining.length()) {
                if (remaining.startsWith("c/", pos)) {
                    int nextPos = findNextPrefix(remaining, pos + 2);
                    company = remaining.substring(pos + 2, nextPos).trim();
                    if (company.isEmpty()) {
                        throw new JobPilotException("Company name cannot be empty!");
                    }
                    pos = nextPos;
                } else if (remaining.startsWith("p/", pos)) {
                    int nextPos = findNextPrefix(remaining, pos + 2);
                    position = remaining.substring(pos + 2, nextPos).trim();
                    if (position.isEmpty()) {
                        throw new JobPilotException("Position cannot be empty!");
                    }
                    pos = nextPos;
                } else if (remaining.startsWith("d/", pos)) {
                    int nextPos = findNextPrefix(remaining, pos + 2);
                    date = remaining.substring(pos + 2, nextPos).trim();
                    if (date.isEmpty()) {
                        throw new JobPilotException("Date cannot be empty!");
                    }
                    pos = nextPos;
                } else if (remaining.startsWith("s/", pos)) {
                    int nextPos = findNextPrefix(remaining, pos + 2);
                    status = remaining.substring(pos + 2, nextPos).trim();
                    if (status.isEmpty()) {
                        throw new JobPilotException("Status cannot be empty!");
                    }
                    pos = nextPos;
                } else {
                    pos++;
                }
            }

            editApplication(index, applications, company, position, date, status);

        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid index! Use a number: edit 1 c/Google");
        }
    }

    /**
     * Main edit method that applies updates to an application.
     * Only non-null fields will be updated.
     *
     * @param index The index of the application to edit (0-based)
     * @param applications The list storing all job applications
     * @param newCompany New company name (null if not updating)
     * @param newPosition New position title (null if not updating)
     * @param newDate New date (null if not updating)
     * @param newStatus New status (null if not updating)
     * @throws JobPilotException If validation fails or no fields to update
     */
    public static void editApplication(int index, ArrayList<Application> applications,
                                       String newCompany, String newPosition,
                                       String newDate, String newStatus)
            throws JobPilotException {

        if (index < 0 || index >= applications.size()) {
            throw new JobPilotException("Invalid application number! You have " +
                            applications.size() + " application(s).");
        }

        Application target = applications.get(index);
        boolean updated = false;

        if (newCompany != null) {
            target.setCompany(newCompany);
            updated = true;
        }
        if (newPosition != null) {
            target.setPosition(newPosition);
            updated = true;
        }
        if (newDate != null) {
            try {
                LocalDate.parse(newDate);
            } catch (DateTimeParseException e) {
                throw new JobPilotException("Invalid date! Use YYYY-MM-DD format (e.g., 2024-09-12)");
            }
            target.setDate(newDate);
            updated = true;
        }
        if (newStatus != null) {
            target.setStatus(newStatus);
            updated = true;
        }

        if (!updated) {
            throw new JobPilotException("No valid fields to update! Use: c/COMPANY, p/POSITION, d/DATE, or s/STATUS");
        }
    }

    /**
     * Finds the next prefix (c/, p/, d/, s/) starting from the given position.
     */
    private static int findNextPrefix(String str, int start) {
        int cIndex = str.indexOf("c/", start);
        int pIndex = str.indexOf("p/", start);
        int dIndex = str.indexOf("d/", start);
        int sIndex = str.indexOf("s/", start);

        int next = str.length();

        if (cIndex != -1 && cIndex < next) {
            next = cIndex;
        }

        if (pIndex != -1 && pIndex < next) {
            next = pIndex;
        }
        if (dIndex != -1 && dIndex < next) {
            next = dIndex;
        }

        if (sIndex != -1 && sIndex < next) {
            next = sIndex;
        }

        return next;
    }
}