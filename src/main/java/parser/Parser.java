package parser;

import exception.JobPilotException;

import parser.subparsers.ApplicationParser;
import parser.subparsers.DeleterParser;
import parser.subparsers.EditorParser;
import parser.subparsers.FilterParser;
import parser.subparsers.SearcherParser;
import parser.subparsers.StatusParser;
import parser.subparsers.TaggerParser;
import ui.Ui;

/**
 * Main parser that routes commands to appropriate subparsers.
 */
public class Parser {

    public static ParsedCommand parse(String input) {
        String trimmed = input.trim();


        String command = trimmed.split(" ")[0];

        switch (command) {
            case "bye":
                return new ParsedCommand(CommandType.BYE);

            case "list":
                return new ParsedCommand(CommandType.LIST);

            case "sort":
                return new ParsedCommand(CommandType.SORT);

            case "help":
                return new ParsedCommand(CommandType.HELP);

            case "add":
                try {
                    return ApplicationParser.parse(trimmed);
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                    break;
                }
            case "filter":
                try {
                    return FilterParser.parse(trimmed);
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                    break;
                }

            case "delete":
                try {
                    return DeleterParser.parse(trimmed);
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                    break;
                }

            case "edit":
                try {
                    return EditorParser.parse(trimmed);
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                    break;
                }

            case "search":
                try {
                    return SearcherParser.parse(trimmed);
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                    break;
                }

            case "status":
                try {
                    return StatusParser.parse(trimmed);
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                    break;
                }

            case "tag":
                try {
                    return TaggerParser.parse(trimmed);
                } catch (JobPilotException e) {
                    Ui.showError(e.getMessage());
                    break;
                }

            default:
                return new ParsedCommand(CommandType.UNKNOWN);
        }
        return null;
    }
}