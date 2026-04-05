package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;

/**
 * Parses the edit command.
 * Format: edit INDEX [c/COMPANY] [p/POSITION] [d/DATE] [s/STATUS]
 */
public class EditorParser {

    public static ParsedCommand parse(String input) throws JobPilotException {
        String normalized = input.trim().replaceAll("\\s+", " ");
        String[] parts = normalized.split(" ");

        if (parts.length < 2) {
            throw new JobPilotException("Please provide an index. Example: edit 1 c/Google");
        }

        int index;
        try {
            index = Integer.parseInt(parts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid index! Use a number: edit 1 c/Google");
        }

        int fieldsStart = normalized.indexOf(" ", normalized.indexOf(" ") + 1) + 1;
        if (fieldsStart <= 0 || fieldsStart >= normalized.length()) {
            throw new JobPilotException("No valid fields to update! Use: c/COMPANY, p/POSITION, d/DATE, or s/STATUS");
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

        return new ParsedCommand(index, company, position, date, status);
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
        if (cIndex != -1 && cIndex < next) next = cIndex;
        if (pIndex != -1 && pIndex < next) next = pIndex;
        if (dIndex != -1 && dIndex < next) next = dIndex;
        if (sIndex != -1 && sIndex < next) next = sIndex;

        return next;
    }
}