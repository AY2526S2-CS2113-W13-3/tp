package parser;

import exception.JobPilotException;
import parser.subparsers.*;

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
                    return new ParsedCommand(CommandType.ERROR, e.getMessage());
                }

            case "delete":
                try {
                    return DeleterParser.parse(trimmed);
                } catch (JobPilotException e) {
                    return new ParsedCommand(CommandType.ERROR, e.getMessage());
                }

            case "edit":
                try {
                    return EditorParser.parse(trimmed);
                } catch (JobPilotException e) {
                    return new ParsedCommand(CommandType.ERROR, e.getMessage());
                }

            case "search":
                try {
                    return SearcherParser.parse(trimmed);
                } catch (JobPilotException e) {
                    return new ParsedCommand(CommandType.ERROR, e.getMessage());
                }

            case "status":
                try {
                    return StatusParser.parse(trimmed);
                } catch (JobPilotException e) {
                    return new ParsedCommand(CommandType.ERROR, e.getMessage());
                }

            case "tag":
                try {
                    return TaggerParser.parse(trimmed);
                } catch (JobPilotException e) {
                    return new ParsedCommand(CommandType.ERROR, e.getMessage());
                }

            default:
                return new ParsedCommand(CommandType.UNKNOWN);
        }
    }
}
