package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Advanced parser for the 'status' command.
 * Supports flexible ordering: status INDEX [set/STATUS] [note/NOTE]
 */
public class StatusParser {
    static {
        Logger.getLogger(StatusParser.class.getName()).setLevel(Level.OFF);
    }
    private static final Logger LOGGER = Logger.getLogger(StatusParser.class.getName());

    private static final String COMMAND_WORD = "status";
    private static final String PREFIX_SET = "s/";
    private static final String PREFIX_NOTE = "note/";

    /**
     * Parses the raw user input into a ParsedCommand object.
     * * @param input The full raw command string.
     * @return A structured command for the CommandRunner.
     * @throws JobPilotException if syntax is invalid or index is non-numeric.
     */
    public static ParsedCommand parse(String input) throws JobPilotException {
        String trimmed = input.trim();

        if (!trimmed.startsWith(COMMAND_WORD)) {
            throw new JobPilotException("Invalid command start! Use: status INDEX set/STATUS note/NOTE");
        }

        String[] tokens = trimmed.split("\\s+");
        if (tokens.length < 2) {
            throw new JobPilotException("Please provide an index. Example: status 1 set/OFFER");
        }

        int index;
        try {
            index = Integer.parseInt(tokens[1]) - 1;
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "User provided non-numeric index: " + tokens[1]);
            throw new JobPilotException("Invalid index! Use a number: status 1 s/OFFER");
        }

        int firstPrefixPos = findFirstPrefixPos(trimmed);
        if (firstPrefixPos == -1) {
            throw new JobPilotException("No status or note provided! Use s/ or note/.");
        }

        String remaining = trimmed.substring(firstPrefixPos);

        String newStatus = null;
        String newNote = null;
        int currentPos = 0;

        while (currentPos < remaining.length()) {
            if (remaining.startsWith(PREFIX_SET, currentPos)) {
                int nextPrefix = findNextPrefix(remaining, currentPos + PREFIX_SET.length());
                newStatus = remaining.substring(currentPos + PREFIX_SET.length(), nextPrefix).trim();

                if (newStatus.isEmpty()) {
                    throw new JobPilotException("Status value cannot be empty!");
                }
                currentPos = nextPrefix;

            } else if (remaining.startsWith(PREFIX_NOTE, currentPos)) {
                int nextPrefix = findNextPrefix(remaining, currentPos + PREFIX_NOTE.length());
                newNote = remaining.substring(currentPos + PREFIX_NOTE.length(), nextPrefix).trim();

                currentPos = nextPrefix;

            } else {
                currentPos++;
            }
        }

        LOGGER.log(Level.INFO, "Parsed status update for index: " + index);
        return new ParsedCommand(index, newStatus, newNote);
    }

    /**
     * Helper to find the very first occurrence of any valid prefix.
     */
    private static int findFirstPrefixPos(String input) {
        int setIdx = input.indexOf(PREFIX_SET);
        int noteIdx = input.indexOf(PREFIX_NOTE);

        if (setIdx == -1) return noteIdx;
        if (noteIdx == -1) return setIdx;
        return Math.min(setIdx, noteIdx);
    }

    /**
     * Finds the next prefix boundary in the remaining string.
     * This is crucial for splitting "s/OFFER note/Interview" correctly.
     */
    private static int findNextPrefix(String str, int start) {
        int nextSet = str.indexOf(PREFIX_SET, start);
        int nextNote = str.indexOf(PREFIX_NOTE, start);

        int minIndex = str.length();
        if (nextSet != -1 && nextSet < minIndex) minIndex = nextSet;
        if (nextNote != -1 && nextNote < minIndex) minIndex = nextNote;

        return minIndex;
    }
}
