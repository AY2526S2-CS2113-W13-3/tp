package parser.subparsers;

import exception.JobPilotException;
import org.junit.jupiter.api.Test;
import parser.ParsedCommand;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearcherParserTest {

    @Test
    void parse_validSinglePrefix_success() throws JobPilotException {
        ParsedCommand cmd = SearcherParser.parse("search c/google");
        assertEquals("c", cmd.getSearchType());
        assertEquals("google", cmd.getSearchTerm());
    }

    @Test
    void parse_multiplePrefixes_throws() {
        assertThrows(JobPilotException.class, () ->
                SearcherParser.parse("search c/google p/intern"));
    }

    @Test
    void parse_emptyTerm_throws() {
        assertThrows(JobPilotException.class, () ->
                SearcherParser.parse("search s/"));
    }
}
