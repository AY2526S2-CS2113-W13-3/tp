package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;

/**
 * Parses the delete command.
 */
public class DeleterParser {

    /**
     * Parses the delete command input provided by the user and converts it into a ParsedCommand.
     *
     * @param input The full user input string for the delete command.
     * @return ParsedCommand containing the zero-based index of the application to delete.
     * @throws JobPilotException If the input does not contain an index or if the index is not a valid integer.
     */
    public static ParsedCommand parse(String input) throws JobPilotException {
        String[] parts = input.trim().split("\\s+");
        if (parts.length < 2) {
            throw new JobPilotException("Please provide an index. Example: delete 1");
        }

        if (parts.length > 2) {
            throw new JobPilotException("Invalid format! Use: delete INDEX");
        }

        try {
            int index = Integer.parseInt(parts[1]) - 1;
            return new ParsedCommand(index);
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid index! Use a number: delete 1");
        }
    }
}
