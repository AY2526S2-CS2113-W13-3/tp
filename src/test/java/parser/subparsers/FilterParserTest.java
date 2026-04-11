package parser.subparsers;

import exception.JobPilotException;
import org.junit.jupiter.api.Test;
import parser.CommandType;
import parser.ParsedCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilterParserTest {
    @Test
    void parse_validInput_success() throws JobPilotException {
        ParsedCommand command = FilterParser.parse("filter s/PENDING");
        assertEquals(CommandType.FILTER, command.getType());
        assertEquals("PENDING", command.getSearchTerm());
    }

    @Test
    void parse_junkBeforePrefix_throwsException() {
        assertThrows(JobPilotException.class, () -> FilterParser.parse("filter abc s/Pending"));
    }

    @Test
    void parse_singleCharFilter_success() throws JobPilotException {
        // Verifies the fix for the 'Status keyword too short' bug
        ParsedCommand command = FilterParser.parse("filter s/X");
        assertEquals("X", command.getSearchTerm());
    }
}