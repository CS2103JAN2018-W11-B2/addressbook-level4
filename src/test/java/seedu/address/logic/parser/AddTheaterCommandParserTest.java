package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_THEATER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.THEATER_DESC_FIVE;
import static seedu.address.logic.commands.CommandTestUtil.THEATER_DESC_THREE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_SENGKANG;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUMOFNEWTHEATERS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUMOFTHEATERS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CINEMA;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddTheaterCommand;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.Theater;
import seedu.address.testutil.CinemaBuilder;

public class AddTheaterCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTheaterCommand.MESSAGE_USAGE);

    private AddTheaterCommandParser parser = new AddTheaterCommandParser();
    private Index targetIndex = INDEX_FIRST_CINEMA;

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_SENGKANG, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + THEATER_DESC_FIVE, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + THEATER_DESC_THREE, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_THEATER_DESC,
                           Theater.MESSAGE_THEATER_CONSTRAINTS); // invalid theater number
    }

    @Test
    public void parse_zeroFieldSpecified_failure() {
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT); // invalid format
    }

    @Test
    public void parse_singleFieldSpecified_success() {

        String userInput = targetIndex.getOneBased() + THEATER_DESC_THREE;
        Cinema cinema = new CinemaBuilder().withTheater(VALID_NUMOFTHEATERS).build();

        AddTheaterCommand expectedCommand = new AddTheaterCommand(targetIndex, cinema.getTheaters().size());

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        // both values are valid but last one is accepted
        String userInput = targetIndex.getOneBased() + THEATER_DESC_THREE + THEATER_DESC_FIVE;
        Cinema newCinema = new CinemaBuilder().withTheater(VALID_NUMOFNEWTHEATERS).build();

        AddTheaterCommand expectedCommand = new AddTheaterCommand(targetIndex, newCinema.getTheaters().size());

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // last value is accepted
        String userInput = targetIndex.getOneBased() + INVALID_THEATER_DESC + THEATER_DESC_THREE;
        Cinema newCinema = new CinemaBuilder().withTheater(VALID_NUMOFTHEATERS).build();

        AddTheaterCommand expectedCommand = new AddTheaterCommand(targetIndex, newCinema.getTheaters().size());
        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
