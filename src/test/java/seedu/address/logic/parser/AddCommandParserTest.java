package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_THEATER_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.THEATER_DESC_FIVE;
import static seedu.address.logic.commands.CommandTestUtil.THEATER_DESC_THREE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUMOFTHEATERS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_SHAW;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.cinema.Address;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.Email;
import seedu.address.model.cinema.Name;
import seedu.address.model.cinema.Phone;
import seedu.address.model.cinema.Theater;
import seedu.address.testutil.CinemaBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Cinema expectedCinema = new CinemaBuilder().withName(VALID_NAME_SHAW).withPhone(VALID_PHONE_SHAW)
                .withEmail(VALID_EMAIL_SHAW).withAddress(VALID_ADDRESS_SHAW)
                .withTheater(VALID_NUMOFTHEATERS).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW
                + ADDRESS_DESC_SHAW + THEATER_DESC_THREE, new AddCommand(expectedCinema));

        // multiple names - last name accepted
        assertParseSuccess(parser, NAME_DESC_GV + NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW
                + ADDRESS_DESC_SHAW + THEATER_DESC_THREE, new AddCommand(expectedCinema));

        // multiple phones - last phone accepted
        assertParseSuccess(parser, NAME_DESC_SHAW + PHONE_DESC_GV + PHONE_DESC_SHAW + EMAIL_DESC_SHAW
                + ADDRESS_DESC_SHAW + THEATER_DESC_THREE, new AddCommand(expectedCinema));

        // multiple emails - last email accepted
        assertParseSuccess(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_GV + EMAIL_DESC_SHAW
                + ADDRESS_DESC_SHAW + THEATER_DESC_THREE, new AddCommand(expectedCinema));

        // multiple addresses - last address accepted
        assertParseSuccess(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW + ADDRESS_DESC_GV
                + ADDRESS_DESC_SHAW + THEATER_DESC_THREE, new AddCommand(expectedCinema));

        // multiple theaters - last theater accepted
        assertParseSuccess(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW + ADDRESS_DESC_SHAW
                + THEATER_DESC_FIVE + THEATER_DESC_THREE, new AddCommand(expectedCinema));

        // multiple tags - all accepted
        Cinema expectedCinemaMultipleTags = new CinemaBuilder().withName(VALID_NAME_SHAW).withPhone(VALID_PHONE_SHAW)
                .withEmail(VALID_EMAIL_SHAW).withAddress(VALID_ADDRESS_SHAW)
                .withTheater(VALID_NUMOFTHEATERS).build();
        assertParseSuccess(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW + ADDRESS_DESC_SHAW
                + THEATER_DESC_THREE, new AddCommand(expectedCinemaMultipleTags));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Cinema expectedCinema = new CinemaBuilder().withName(VALID_NAME_GV).withPhone(VALID_PHONE_GV)
                .withEmail(VALID_EMAIL_GV).withAddress(VALID_ADDRESS_GV)
                .withTheater(VALID_NUMOFTHEATERS).build();
        assertParseSuccess(parser, NAME_DESC_GV + PHONE_DESC_GV + EMAIL_DESC_GV + ADDRESS_DESC_GV
                + THEATER_DESC_THREE, new AddCommand(expectedCinema));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW
                + ADDRESS_DESC_SHAW + THEATER_DESC_THREE, expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_SHAW + VALID_PHONE_SHAW + EMAIL_DESC_SHAW
                + ADDRESS_DESC_SHAW + THEATER_DESC_THREE, expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + VALID_EMAIL_SHAW
                + ADDRESS_DESC_SHAW + THEATER_DESC_THREE, expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW
                + VALID_ADDRESS_SHAW + THEATER_DESC_THREE, expectedMessage);

        // missing theater prefix
        assertParseFailure(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW
                + ADDRESS_DESC_SHAW + VALID_NUMOFTHEATERS, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_SHAW + VALID_PHONE_SHAW + VALID_EMAIL_SHAW
                + VALID_ADDRESS_SHAW + VALID_NUMOFTHEATERS, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_SHAW + EMAIL_DESC_SHAW + ADDRESS_DESC_SHAW
                + THEATER_DESC_THREE, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_SHAW + INVALID_PHONE_DESC + EMAIL_DESC_SHAW + ADDRESS_DESC_SHAW
                + THEATER_DESC_THREE, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + INVALID_EMAIL_DESC + ADDRESS_DESC_SHAW
                + THEATER_DESC_THREE, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW + INVALID_ADDRESS_DESC
                + THEATER_DESC_THREE, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid theater
        assertParseFailure(parser, NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW + ADDRESS_DESC_SHAW
                + INVALID_THEATER_DESC, Theater.MESSAGE_THEATER_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_SHAW + EMAIL_DESC_SHAW
                + INVALID_ADDRESS_DESC + THEATER_DESC_THREE, Name.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW
                + ADDRESS_DESC_SHAW + THEATER_DESC_THREE,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
    }
}
