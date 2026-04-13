package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;

/**
 * Parses the search command.
 * Supports one field prefix (c/p/s) with case-insensitive partial matching.
 */
public class SearcherParser {
    private static final String SEARCH_MULTI_PREFIX_ERROR =
            "Search supports one prefix per command only. Use: search c/xxx or p/xxx or s/xxx";

    public static ParsedCommand parse(String input) throws JobPilotException {
        if (input == null || input.trim().isEmpty()) {
            throw new JobPilotException("Please provide a search query. Example: search c/google");
        }

        String args = input.substring("search".length()).trim();

        if (args.isEmpty()) {
            throw new JobPilotException("Please provide a search query. Example: search c/google");
        }

        if (!args.contains("/")) {
            throw new JobPilotException("Invalid format! Use: search c/xxx or p/xxx or s/xxx");
        }

        int slashIndex = args.indexOf("/");

        String type = args.substring(0, slashIndex).trim().toLowerCase();
        String value = args.substring(slashIndex + 1).trim().toLowerCase();
        if (containsAnotherPrefix(value)) {
            throw new JobPilotException(SEARCH_MULTI_PREFIX_ERROR);
        }

        if (value.isEmpty()) {
            throw new JobPilotException("Search value cannot be empty!");
        }

        if (!type.equals("c") && !type.equals("p") && !type.equals("s")) {
            throw new JobPilotException("Invalid search type! Use c/, p/, or s/");
        }

        return new ParsedCommand(type, value);
    }

    private static boolean containsAnotherPrefix(String value) {
        return value.contains(" c/") || value.contains(" p/") || value.contains(" s/");
    }
}