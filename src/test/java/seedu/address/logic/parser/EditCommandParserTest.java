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
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_SHAW;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CINEMA;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CINEMA;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_CINEMA;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditCinemaDescriptor;
import seedu.address.model.cinema.Address;
import seedu.address.model.cinema.Email;
import seedu.address.model.cinema.Name;
import seedu.address.model.cinema.Phone;
import seedu.address.testutil.EditCinemaDescriptorBuilder;

public class EditCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_GV, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_GV, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_GV, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_NAME_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_PHONE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_EMAIL_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADDRESS_DESC, Address.MESSAGE_ADDRESS_CONSTRAINTS); // invalid address

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_GV, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // valid phone followed by invalid phone. The test case for invalid phone followed by valid phone
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + PHONE_DESC_SHAW + INVALID_PHONE_DESC, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_ADDRESS_GV + VALID_PHONE_GV,
                Name.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_CINEMA;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_SHAW
                + EMAIL_DESC_GV + ADDRESS_DESC_GV + NAME_DESC_GV;

        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder().withName(VALID_NAME_GV)
                .withPhone(VALID_PHONE_SHAW).withEmail(VALID_EMAIL_GV).withAddress(VALID_ADDRESS_GV)
                .build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_CINEMA;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_SHAW + EMAIL_DESC_GV;

        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder().withPhone(VALID_PHONE_SHAW)
                .withEmail(VALID_EMAIL_GV).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_CINEMA;
        String userInput = targetIndex.getOneBased() + NAME_DESC_GV;
        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder().withName(VALID_NAME_GV).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_GV;
        descriptor = new EditCinemaDescriptorBuilder().withPhone(VALID_PHONE_GV).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_GV;
        descriptor = new EditCinemaDescriptorBuilder().withEmail(VALID_EMAIL_GV).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // address
        userInput = targetIndex.getOneBased() + ADDRESS_DESC_GV;
        descriptor = new EditCinemaDescriptorBuilder().withAddress(VALID_ADDRESS_GV).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_CINEMA;
        String userInput = targetIndex.getOneBased()  + PHONE_DESC_GV + ADDRESS_DESC_GV + EMAIL_DESC_GV
                + PHONE_DESC_GV + ADDRESS_DESC_GV + EMAIL_DESC_GV
                + PHONE_DESC_SHAW + ADDRESS_DESC_SHAW + EMAIL_DESC_SHAW;

        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder().withPhone(VALID_PHONE_SHAW)
                .withEmail(VALID_EMAIL_SHAW).withAddress(VALID_ADDRESS_SHAW).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_CINEMA;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_SHAW;
        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder().withPhone(VALID_PHONE_SHAW).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + EMAIL_DESC_SHAW + INVALID_PHONE_DESC + ADDRESS_DESC_SHAW
                + PHONE_DESC_SHAW;
        descriptor = new EditCinemaDescriptorBuilder().withPhone(VALID_PHONE_SHAW).withEmail(VALID_EMAIL_SHAW)
                .withAddress(VALID_ADDRESS_SHAW).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
