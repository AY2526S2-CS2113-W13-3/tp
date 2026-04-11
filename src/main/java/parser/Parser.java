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

    /**
     * Commands that must be a single word with no trailing arguments.
     */
    private static boolean isLoneCommand(String trimmed, String commandWord) {
        String[] parts = trimmed.split("\\s+");
        return parts.length == 1 && parts[0].equals(commandWord);
    }

    private static ParsedCommand rejectExtraArgs(String commandWord) {
        Ui.showError("The '" + commandWord + "' command does not take any arguments.");
        return null;
    }

    public static ParsedCommand parse(String input) {
        String trimmed = input.trim();


        String command = trimmed.split(" ")[0];

        switch (command) {
            case "bye":
                if (!isLoneCommand(trimmed, "bye")) {
                    return rejectExtraArgs("bye");
                }
                return new ParsedCommand(CommandType.BYE);

            case "list":
                if (!isLoneCommand(trimmed, "list")) {
                    return rejectExtraArgs("list");
                }
                return new ParsedCommand(CommandType.LIST);

            case "sort":
                if (!isLoneCommand(trimmed, "sort")) {
                    return rejectExtraArgs("sort");
                }
                return new ParsedCommand(CommandType.SORT);

            case "help":
                if (!isLoneCommand(trimmed, "help")) {
                    return rejectExtraArgs("help");
                }
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