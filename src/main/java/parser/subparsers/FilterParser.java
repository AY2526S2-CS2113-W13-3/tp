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

    private static final String COMMAND_WORD = "filter";
    private static final String PREFIX_STATUS = "s/"; // Updated to match team master

    private static final String ERROR_INVALID_FORMAT = "Invalid filter format! Expected: filter s/STATUS";
    private static final String ERROR_MISSING_ARGS = "Filter command is missing arguments! Use: filter s/STATUS";
    private static final String ERROR_EMPTY_VALUE = "The filter value cannot be empty! Please provide a status after 's/'.";

    /**
     * Parses the 'filter' command input string into a ParsedCommand object.
     */
    public static ParsedCommand parse(String input) throws JobPilotException {
        LOGGER.log(Level.INFO, "Initiating parsing for filter command: " + input);

        String trimmedInput = input.trim();
        validateCommandStart(trimmedInput);

        String argumentBlock = extractArgumentBlock(trimmedInput);

        if (!argumentBlock.contains(PREFIX_STATUS)) {
            LOGGER.log(Level.WARNING, "Filter command missing required prefix 's/': " + argumentBlock);
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
        if (input.length() <= COMMAND_WORD.length()) {
            throw new JobPilotException(ERROR_MISSING_ARGS);
        }
        return input.substring(COMMAND_WORD.length()).trim();
    }

    private static String extractStatusQuery(String argumentBlock) {
        int startIndex = argumentBlock.indexOf(PREFIX_STATUS) + PREFIX_STATUS.length();
        String query = argumentBlock.substring(startIndex).trim();
        return query.replaceAll("\\s+", " ");
    }
}