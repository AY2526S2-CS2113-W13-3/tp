package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatusParser {

    private static final Logger LOGGER = Logger.getLogger(StatusParser.class.getName());

    static {
        LOGGER.setLevel(Level.OFF);
        LOGGER.setUseParentHandlers(false);
    }

    private static final String COMMAND_WORD = "status";
    private static final String PREFIX_SET = "s/";
    private static final String PREFIX_NOTE = "note/";

    public static ParsedCommand parse(String input) throws JobPilotException {
        String trimmed = input.trim();

        if (!trimmed.toLowerCase().startsWith(COMMAND_WORD)) {
            throw new JobPilotException("Invalid command start!");
        }

        String[] tokens = trimmed.split("\\s+");
        if (tokens.length < 2) {
            throw new JobPilotException("Please provide an index. Example: status 1 s/OFFER");
        }

        int index;
        try {
            index = Integer.parseInt(tokens[1]) - 1;
        } catch (NumberFormatException e) {
            throw new JobPilotException("Invalid index! Use a number: status 1 s/OFFER");
        }

        int firstPrefixPos = findFirstPrefixPos(trimmed);
        if (firstPrefixPos == -1) {
            throw new JobPilotException("No status or note provided! Use s/ or note/.");
        }

        // Junk Zone Check
        int endOfIndexPos = trimmed.indexOf(tokens[1]) + tokens[1].length();
        String junkZone = trimmed.substring(endOfIndexPos, firstPrefixPos).trim();
        if (!junkZone.isEmpty()) {
            throw new JobPilotException("Invalid format! Unexpected text before prefixes: " + junkZone);
        }

        String remaining = trimmed.substring(firstPrefixPos);
        String newStatus = null;
        String newNote = null;
        int currentPos = 0;

        while (currentPos < remaining.length()) {
            if (remaining.startsWith(PREFIX_SET, currentPos)) {
                if (newStatus != null) throw new JobPilotException("Duplicate prefix: " + PREFIX_SET);

                int nextPrefix = findNextPrefix(remaining, currentPos + PREFIX_SET.length());
                newStatus = remaining.substring(currentPos + PREFIX_SET.length(), nextPrefix).trim();

                if (newStatus.isEmpty()) throw new JobPilotException("Status value cannot be empty!");
                currentPos = nextPrefix;

            } else if (remaining.startsWith(PREFIX_NOTE, currentPos)) {
                if (newNote != null) throw new JobPilotException("Duplicate prefix: " + PREFIX_NOTE);

                int nextPrefix = findNextPrefix(remaining, currentPos + PREFIX_NOTE.length());
                newNote = remaining.substring(currentPos + PREFIX_NOTE.length(), nextPrefix).trim();
                currentPos = nextPrefix;

            } else {
                currentPos++;
            }
        }

        return new ParsedCommand(index, newStatus, newNote);
    }

    /**
     * Finds the next prefix only if it is preceded by whitespace.
     * This prevents "s/s/" from breaking the parser.
     */
    private static int findNextPrefix(String str, int start) {
        int nextSet = findPrefixWithSpace(str, PREFIX_SET, start);
        int nextNote = findPrefixWithSpace(str, PREFIX_NOTE, start);

        int minIndex = str.length();
        if (nextSet != -1 && nextSet < minIndex) minIndex = nextSet;
        if (nextNote != -1 && nextNote < minIndex) minIndex = nextNote;
        return minIndex;
    }

    private static int findPrefixWithSpace(String str, String prefix, int start) {
        int pos = str.indexOf(prefix, start);
        while (pos != -1) {
            if (pos == 0 || Character.isWhitespace(str.charAt(pos - 1))) {
                return pos;
            }
            pos = str.indexOf(prefix, pos + 1);
        }
        return -1;
    }

    private static int findFirstPrefixPos(String input) {
        int setIdx = findPrefixWithSpace(input, PREFIX_SET, 0);
        int noteIdx = findPrefixWithSpace(input, PREFIX_NOTE, 0);
        if (setIdx == -1) return noteIdx;
        if (noteIdx == -1) return setIdx;
        return Math.min(setIdx, noteIdx);
    }
}
