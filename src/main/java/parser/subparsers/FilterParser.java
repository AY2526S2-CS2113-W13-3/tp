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
 * * Expected Format: filter status/STATUS
 * This parser is designed to be extensible for future filters (e.g., filter date/YYYY-MM-DD).
 */
public class FilterParser {

    private static final Logger LOGGER = Logger.getLogger(FilterParser.class.getName());

    // Command and Prefix Constants
    private static final String COMMAND_WORD = "filter";
    private static final String PREFIX_STATUS = "status/";

    // Error Messages
    private static final String ERROR_MISSING_ARGS = "Filter command is missing arguments! "
            + "Use: filter status/STATUS";
    private static final String ERROR_INVALID_FORMAT = "Invalid filter format! "
            + "Expected: filter status/STATUS";
    private static final String ERROR_EMPTY_VALUE = "The filter value cannot be empty! "
            + "Please provide a status after 'status/'.";

    /**
     * Parses the 'filter' command input string into a ParsedCommand object.
     * * @param input The full raw command string starting with "filter".
     * @return A ParsedCommand containing the filter criteria.
     * @throws JobPilotException If the format is invalid or the status value is empty.
     */
    public static ParsedCommand parse(String input) throws JobPilotException {
        LOGGER.log(Level.INFO, "Initiating parsing for filter command: " + input);

        // 1. Basic validation and normalization
        String trimmedInput = input.trim();
        validateCommandStart(trimmedInput);

        // 2. Extract the argument block (everything after "filter")
        String argumentBlock = extractArgumentBlock(trimmedInput);

        // 3. Check for the specific 'status/' prefix
        // We use a modular approach here to allow adding more prefixes (e.g., company/) later
        if (!argumentBlock.contains(PREFIX_STATUS)) {
            LOGGER.log(Level.WARNING, "Filter command missing required prefix 'status/': " + argumentBlock);
            throw new JobPilotException(ERROR_INVALID_FORMAT);
        }

        // 4. Extract and sanitize the status query
        String statusQuery = extractStatusQuery(argumentBlock);

        // 5. Final validation of the extracted value
        if (statusQuery.isEmpty()) {
            LOGGER.log(Level.INFO, "User provided prefix 'status/' but no actual value.");
            throw new JobPilotException(ERROR_EMPTY_VALUE);
        }

        LOGGER.log(Level.FINE, "Successfully parsed filter command with query: " + statusQuery);

        // Return using the general constructor (Type, StringValue)
        return new ParsedCommand(CommandType.FILTER, statusQuery);
    }

    /**
     * Ensures the input string actually begins with the 'filter' keyword.
     */
    private static void validateCommandStart(String input) throws JobPilotException {
        if (!input.toLowerCase().startsWith(COMMAND_WORD)) {
            throw new JobPilotException("Internal Error: FilterParser called for non-filter command.");
        }
    }

    /**
     * Safely isolates the arguments from the command word.
     */
    private static String extractArgumentBlock(String input) throws JobPilotException {
        if (input.length() <= COMMAND_WORD.length()) {
            throw new JobPilotException(ERROR_MISSING_ARGS);
        }

        String args = input.substring(COMMAND_WORD.length()).trim();
        if (args.isEmpty()) {
            throw new JobPilotException(ERROR_MISSING_ARGS);
        }
        return args;
    }

    /**
     * Specifically extracts the value associated with the 'status/' prefix.
     * Handles cases where 'status/' might be followed by other future flags.
     */
    private static String extractStatusQuery(String argumentBlock) {
        int startIndex = argumentBlock.indexOf(PREFIX_STATUS) + PREFIX_STATUS.length();

        // Find if there is another space or prefix following this one (for future proofing)
        int nextSpace = argumentBlock.indexOf(" ", startIndex);

        String query;
        if (nextSpace != -1) {
            query = argumentBlock.substring(startIndex, nextSpace).trim();
        } else {
            query = argumentBlock.substring(startIndex).trim();
        }

        // We sanitize the query to ensure it doesn't contain leading/trailing junk
        return sanitizeQuery(query);
    }

    /**
     * Cleans the query string to prevent issues with case sensitivity or extra whitespace.
     */
    private static String sanitizeQuery(String query) {
        if (query == null) {
            return "";
        }
        // Trim and remove any accidental double spaces
        return query.trim().replaceAll("\\s+", " ");
    }
}
// @@author