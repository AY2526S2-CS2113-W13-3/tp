package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;

/**
 * Parses the search command.
 * Format: search COMPANY_NAME
 */
public class SearcherParser {

    public static ParsedCommand parse(String input) throws JobPilotException {
        if (input == null || input.trim().isEmpty()) {
            throw new JobPilotException("Please provide a company name to search. Example: search google");
        }

        String searchTerm = input.substring("search".length()).trim();

        if (searchTerm.isEmpty()) {
            throw new JobPilotException("Please provide a company name to search. Example: search google");
        }

        return new ParsedCommand(parser.CommandType.SEARCH, searchTerm);
    }
}