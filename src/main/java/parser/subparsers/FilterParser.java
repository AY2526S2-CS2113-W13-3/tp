package parser.subparsers;

import exception.JobPilotException;
import parser.CommandType;
import parser.ParsedCommand;

import java.util.logging.Level;
import java.util.logging.Logger;

// @@author Aswin-RajeshKumar
/**
 * Advanced parser for the 'filter' command in JobPilot.
 * Extracts search criteria to narrow down the application list.
 * Expected Format: filter s/STATUS
 */
public class FilterParser {

    private static final Logger LOGGER = Logger.getLogger(FilterParser.class.getName());

    static {
        LOGGER.setLevel(Level.OFF);
        LOGGER.setUseParentHandlers(false);
    }

    private static final String COMMAND_WORD = "filter";
    private static final String PREFIX_STATUS = "s/";

    private static final String ERROR_INVALID_FORMAT = "Invalid filter format! Expected: filter s/STATUS";
    private static final String ERROR_MISSING_ARGS = "Filter command is missing arguments! Use: filter s/STATUS";
    private static final String ERROR_EMPTY_VALUE = "The filter value cannot be empty! Please provide a status after 's/'.";

    /**
     * Parses the 'filter' command input string into a ParsedCommand object.
     * Validates that the input strictly follows the 'filter s/STATUS' format.
     * * @param input The raw arguments after the command word 'filter'
     * @return A ParsedCommand ready for execution
     * @throws JobPilotException if the format is invalid or contains junk text before the prefix
     */
    public static ParsedCommand parse(String input) throws JobPilotException {
        LOGGER.log(Level.INFO, "Initiating parsing for filter command: " + input);

        String trimmedInput = input.trim();
        validateCommandStart(trimmedInput);

        String argumentBlock = extractArgumentBlock(trimmedInput);

        if (!argumentBlock.startsWith(PREFIX_STATUS)) {
            LOGGER.log(Level.WARNING, "Filter command has invalid text before prefix: " + argumentBlock);
            throw new JobPilotException(ERROR_INVALID_FORMAT);
        }

        String statusQuery = extractStatusQuery(argumentBlock);

        if (statusQuery.isEmpty()) {
            throw new JobPilotException(ERROR_EMPTY_VALUE);
        }

        LOGGER.log(Level.FINE, "Successfully parsed filter command with query: " + statusQuery);
        return new ParsedCommand(CommandType.FILTER, statusQuery);
    }

    private static void validateCommandStart(String input) throws JobPilotException {
        if (!input.toLowerCase().startsWith(COMMAND_WORD)) {
            throw new JobPilotException("Internal Error: FilterParser called for non-filter command.");
        }
    }

    private static String extractArgumentBlock(String input) throws JobPilotException {
        // Ensure there is at least one space or character after "filter"
        if (input.length() <= COMMAND_WORD.length()) {
            throw new JobPilotException(ERROR_MISSING_ARGS);
        }
        return input.substring(COMMAND_WORD.length()).trim();
    }

    private static String extractStatusQuery(String argumentBlock) {
        int startIndex = PREFIX_STATUS.length();
        String query = argumentBlock.substring(startIndex).trim();
        return query.replaceAll("\\s+", " ");
    }
}
