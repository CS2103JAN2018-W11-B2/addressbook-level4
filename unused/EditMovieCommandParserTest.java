package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DURATION_DESC_INCREDIBLES;
import static seedu.address.logic.commands.CommandTestUtil.DURATION_DESC_MARVEL;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_DURATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_MOVIENAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_RATING_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_STARTDATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.MOVIENAME_DESC_INCREDIBLES;
import static seedu.address.logic.commands.CommandTestUtil.MOVIENAME_DESC_MARVEL;
import static seedu.address.logic.commands.CommandTestUtil.RATING_DESC_INCREDIBLES;
import static seedu.address.logic.commands.CommandTestUtil.RATING_DESC_MARVEL;
import static seedu.address.logic.commands.CommandTestUtil.STARTDATE_DESC_INCREDIBLES;
import static seedu.address.logic.commands.CommandTestUtil.STARTDATE_DESC_MARVEL;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_COMEDY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_SUPERHERO;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DURATION_INCREDIBLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DURATION_MARVEL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MOVIENAME_INCREDIBLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MOVIENAME_MARVEL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RATING_INCREDIBLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RATING_MARVEL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STARTDATE_INCREDIBLES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_STARTDATE_MARVEL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_COMEDY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_SUPERHERO;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_MOVIE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_MOVIE;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_MOVIE;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditMovieCommand;
import seedu.address.logic.commands.EditMovieCommand.EditMovieDescriptor;
import seedu.address.model.movie.Duration;
import seedu.address.model.movie.MovieName;
import seedu.address.model.movie.Rating;
import seedu.address.model.movie.StartDate;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditMovieDescriptorBuilder;
//@@author slothhy-unused
/**
 * This is a test for EditMovie
 */
public class EditMovieCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditMovieCommand.MESSAGE_USAGE);

    private EditMovieCommandParser parser = new EditMovieCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_MOVIENAME_INCREDIBLES, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditMovieCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + MOVIENAME_DESC_MARVEL, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + MOVIENAME_DESC_INCREDIBLES, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid movieName
        assertParseFailure(parser, "1" + INVALID_MOVIENAME_DESC, MovieName.MESSAGE_MOVIENAME_CONSTRAINTS);
        // invalid duration
        assertParseFailure(parser, "1" + INVALID_DURATION_DESC, Duration.MESSAGE_DURATION_CONSTRAINTS);
        // invalid rating
        assertParseFailure(parser, "1" + INVALID_RATING_DESC, Rating.MESSAGE_RATING_CONSTRAINTS);
        // invalid startDate
        assertParseFailure(parser, "1" + INVALID_STARTDATE_DESC, StartDate.MESSAGE_STARTDATE_CONSTRAINTS);
        // invalid tag
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);

        // invalid duration followed by valid email
        assertParseFailure(parser, "1" + INVALID_DURATION_DESC + RATING_DESC_MARVEL,
                Duration.MESSAGE_DURATION_CONSTRAINTS);

        // valid duration followed by invalid duration. The test case for invalid duration followed by valid duration
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + DURATION_DESC_INCREDIBLES + INVALID_DURATION_DESC,
                Duration.MESSAGE_DURATION_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_MOVIENAME_DESC + INVALID_DURATION_DESC
                        + VALID_RATING_INCREDIBLES + VALID_DURATION_INCREDIBLES,
                MovieName.MESSAGE_MOVIENAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_MOVIE;
        String userInput = targetIndex.getOneBased() + RATING_DESC_INCREDIBLES
                + DURATION_DESC_MARVEL + STARTDATE_DESC_MARVEL + MOVIENAME_DESC_MARVEL + TAG_DESC_SUPERHERO;

        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withName(VALID_MOVIENAME_MARVEL)
                .withDuration(VALID_DURATION_MARVEL).withRating(VALID_RATING_INCREDIBLES)
                .withStartDate(VALID_STARTDATE_MARVEL).withTags(VALID_TAG_SUPERHERO).build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_MOVIE;
        String userInput = targetIndex.getOneBased() + DURATION_DESC_INCREDIBLES + RATING_DESC_MARVEL;

        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_INCREDIBLES)
                .withRating(VALID_RATING_MARVEL).build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_MOVIE;
        String userInput = targetIndex.getOneBased() + MOVIENAME_DESC_INCREDIBLES;
        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withName(VALID_MOVIENAME_INCREDIBLES).build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // duration
        userInput = targetIndex.getOneBased() + DURATION_DESC_MARVEL;
        descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_MARVEL).build();
        expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // rating
        userInput = targetIndex.getOneBased() + RATING_DESC_MARVEL;
        descriptor = new EditMovieDescriptorBuilder().withRating(VALID_RATING_MARVEL).build();
        expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // startDate
        userInput = targetIndex.getOneBased() + STARTDATE_DESC_MARVEL;
        descriptor = new EditMovieDescriptorBuilder().withStartDate(VALID_STARTDATE_MARVEL).build();
        expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_MOVIE;
        String userInput = targetIndex.getOneBased()  + DURATION_DESC_MARVEL + STARTDATE_DESC_MARVEL
                + RATING_DESC_MARVEL + DURATION_DESC_MARVEL + STARTDATE_DESC_MARVEL + RATING_DESC_MARVEL
                + DURATION_DESC_INCREDIBLES + STARTDATE_DESC_INCREDIBLES + RATING_DESC_INCREDIBLES + TAG_DESC_COMEDY;

        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_INCREDIBLES)
                .withRating(VALID_RATING_INCREDIBLES).withStartDate(VALID_STARTDATE_INCREDIBLES)
                .withTags(VALID_TAG_COMEDY).build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_MOVIE;
        String userInput = targetIndex.getOneBased() + INVALID_DURATION_DESC + DURATION_DESC_INCREDIBLES;
        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_INCREDIBLES)
                .build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + RATING_DESC_INCREDIBLES + INVALID_DURATION_DESC
                + STARTDATE_DESC_INCREDIBLES + DURATION_DESC_INCREDIBLES;
        descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_INCREDIBLES)
                .withRating(VALID_RATING_INCREDIBLES).withStartDate(VALID_STARTDATE_INCREDIBLES).build();
        expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_MOVIE;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withTags().build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
