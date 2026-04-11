package parser.subparsers;

import exception.JobPilotException;
import parser.ParsedCommand;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Advanced parser for the 'status' command.
 * Supports flexible ordering: status INDEX [s/STATUS] [note/NOTE]
 * Ensures strict formatting by rejecting unexpected text between arguments.
 */
public class StatusParser {

    private static final Logger LOGGER = Logger.getLogger(StatusParser.class.getName());

    static {
        LOGGER.setLevel(Level.OFF);
        // Prevents log messages from leaking into the console during runtest.bat
        LOGGER.setUseParentHandlers(false);
    }

    private static final String COMMAND_WORD = "status";
    private static final String PREFIX_SET = "s/";
    private static final String PREFIX_NOTE = "note/";

    /**
     * Parses the raw user input into a ParsedCommand object.
     * * @param input The full raw command string.
     * @return A structured command for the CommandRunner.
     * @throws JobPilotException if syntax is invalid, junk text is found, or index is non-numeric.
     */
    public static ParsedCommand parse(String input) throws JobPilotException {
        String trimmed = input.trim();

        if (!trimmed.toLowerCase().startsWith(COMMAND_WORD)) {
            throw new JobPilotException("Invalid command start! Use: status INDEX s/STATUS note/NOTE");
        }

        String[] tokens = trimmed.split("\\s+");
        if (tokens.length < 2) {
            throw new JobPilotException("Please provide an index. Example: status 1 s/OFFER");
        }

        // Extract and validate index
        int index;
        try {
            index = Integer.parseInt(tokens[1]) - 1;
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "User provided non-numeric index: " + tokens[1]);
            throw new JobPilotException("Invalid index! Use a number: status 1 s/OFFER");
        }

        // Find prefix boundaries
        int firstPrefixPos = findFirstPrefixPos(trimmed);
        if (firstPrefixPos == -1) {
            throw new JobPilotException("No status or note provided! Use s/ or note/.");
        }

        // --- BUG FIX: Junk Zone Validation ---
        // Find where the index ends and check if there's unexpected text before the first prefix
        int indexStartPos = trimmed.indexOf(tokens[1]);
        int endOfIndexPos = indexStartPos + tokens[1].length();

        String junkZone = trimmed.substring(endOfIndexPos, firstPrefixPos).trim();
        if (!junkZone.isEmpty()) {
            // This catches "randomtext" in "status 1 randomtext note/ok"
            throw new JobPilotException("Invalid format! Unexpected text found before prefixes: " + junkZone);
        }
        // --- END BUG FIX ---

        String remaining = trimmed.substring(firstPrefixPos);
        String newStatus = null;
        String newNote = null;
        int currentPos = 0;

        // Parse s/ and note/ flags
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
                // Skip characters that aren't part of a known prefix start
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

        if (setIdx == -1) {
            return noteIdx;
        }
        if (noteIdx == -1) {
            return setIdx;
        }
        return Math.min(setIdx, noteIdx);
    }

    /**
     * Finds the next prefix boundary in the remaining string.
     */
    private static int findNextPrefix(String str, int start) {
        int nextSet = str.indexOf(PREFIX_SET, start);
        int nextNote = str.indexOf(PREFIX_NOTE, start);

        int minIndex = str.length();
        if (nextSet != -1 && nextSet < minIndex) {
            minIndex = nextSet;
        }
        if (nextNote != -1 && nextNote < minIndex) {
            minIndex = nextNote;
        }

        return minIndex;
    }
}
