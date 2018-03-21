package systemtests;

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
import static seedu.address.logic.commands.CommandTestUtil.THEATER_DESC_FIVE;
import static seedu.address.logic.commands.CommandTestUtil.THEATER_DESC_THREE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUMOFNEWTHEATERS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NUMOFTHEATERS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_GV;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_SHAW;
import static seedu.address.testutil.TypicalCinemas.CATHAY_AMK_HUB;
import static seedu.address.testutil.TypicalCinemas.CATHAY_JEM;
import static seedu.address.testutil.TypicalCinemas.GV;
import static seedu.address.testutil.TypicalCinemas.GV_PAYA_LEBAR;
import static seedu.address.testutil.TypicalCinemas.GV_VIVOCITY;
import static seedu.address.testutil.TypicalCinemas.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalCinemas.SHAW;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.cinema.Address;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.Email;
import seedu.address.model.cinema.Name;
import seedu.address.model.cinema.Phone;
import seedu.address.model.cinema.Theater;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;
import seedu.address.testutil.CinemaBuilder;
import seedu.address.testutil.CinemaUtil;

public class AddCommandSystemTest extends MoviePlannerSystemTest {

    @Test
    public void add() throws Exception {
        Model model = getModel();

        /* ------------------------ Perform add operations on the shown unfiltered list ----------------------------- */

        /* Case: add a cinema to a non-empty movie planner, command with leading spaces and trailing spaces
         * -> added
         */
        Cinema toAdd = GV;
        String command = "   " + AddCommand.COMMAND_WORD + "  " + NAME_DESC_GV + "  " + PHONE_DESC_GV + " "
                + EMAIL_DESC_GV + "   " + ADDRESS_DESC_GV + "   "  + THEATER_DESC_THREE + " ";
        assertCommandSuccess(command, toAdd);

        /* Case: undo adding GV to the list -> GV deleted */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo adding GV to the list -> GV added again */
        command = RedoCommand.COMMAND_WORD;
        model.addCinema(toAdd);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: add a cinema with all fields same as another cinema in the movie planner except name -> added */
        toAdd = new CinemaBuilder().withName(VALID_NAME_SHAW).withPhone(VALID_PHONE_GV).withEmail(VALID_EMAIL_GV)
                .withAddress(VALID_ADDRESS_GV).withTheater(VALID_NUMOFTHEATERS).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_SHAW + PHONE_DESC_GV + EMAIL_DESC_GV + ADDRESS_DESC_GV
                + THEATER_DESC_THREE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a cinema with all fields same as another cinema in the movie planner except phone -> added */
        toAdd = new CinemaBuilder().withName(VALID_NAME_GV).withPhone(VALID_PHONE_SHAW).withEmail(VALID_EMAIL_GV)
                .withAddress(VALID_ADDRESS_GV).withTheater(VALID_NUMOFTHEATERS).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_SHAW + EMAIL_DESC_GV + ADDRESS_DESC_GV
                + THEATER_DESC_THREE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a cinema with all fields same as another cinema in the movie planner except email -> added */
        toAdd = new CinemaBuilder().withName(VALID_NAME_GV).withPhone(VALID_PHONE_GV).withEmail(VALID_EMAIL_SHAW)
                .withAddress(VALID_ADDRESS_GV).withTheater(VALID_NUMOFTHEATERS).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_GV + EMAIL_DESC_SHAW + ADDRESS_DESC_GV
                + THEATER_DESC_THREE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a cinema with all fields same as another cinema in the movie planner except address -> added */
        toAdd = new CinemaBuilder().withName(VALID_NAME_GV).withPhone(VALID_PHONE_GV).withEmail(VALID_EMAIL_GV)
                .withAddress(VALID_ADDRESS_SHAW).withTheater(VALID_NUMOFTHEATERS).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_GV + EMAIL_DESC_GV + ADDRESS_DESC_SHAW
                + THEATER_DESC_THREE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a cinema with all fields same as another cinema in the movie planner except theater -> added */
        toAdd = new CinemaBuilder().withName(VALID_NAME_GV).withPhone(VALID_PHONE_GV).withEmail(VALID_EMAIL_GV)
                .withAddress(VALID_ADDRESS_GV).withTheater(VALID_NUMOFNEWTHEATERS).build();
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_GV + EMAIL_DESC_GV + ADDRESS_DESC_GV
                + THEATER_DESC_FIVE;
        assertCommandSuccess(command, toAdd);

        /* Case: add to empty movie planner -> added */
        deleteAllCinemas();
        assertCommandSuccess(GV_PAYA_LEBAR);

        /* Case: add a cinema command with parameters in random order -> added */
        toAdd = SHAW;
        command = AddCommand.COMMAND_WORD + PHONE_DESC_SHAW + ADDRESS_DESC_SHAW + NAME_DESC_SHAW
                + EMAIL_DESC_SHAW + THEATER_DESC_THREE;
        assertCommandSuccess(command, toAdd);

        /* Case: add a cinema, -> added */
        assertCommandSuccess(GV_VIVOCITY);

        /* -------------------------- Perform add operation on the shown filtered list ------------------------------ */

        /* Case: filters the cinema list before adding -> added */
        showCinemasWithName(KEYWORD_MATCHING_MEIER);
        assertCommandSuccess(CATHAY_JEM);

        /* ------------------------ Perform add operation while a cinema card is selected --------------------------- */

        /* Case: selects first card in the cinema list, add a cinema -> added, card selection remains unchanged */
        selectCinema(Index.fromOneBased(1));
        assertCommandSuccess(CATHAY_AMK_HUB);

        /* ----------------------------------- Perform invalid add operations --------------------------------------- */

        /* Case: add a duplicate cinema -> rejected */
        command = CinemaUtil.getAddCommand(GV_VIVOCITY);
        assertCommandFailure(command, AddCommand.MESSAGE_DUPLICATE_CINEMA);

        /* Case: missing name -> rejected */
        command = AddCommand.COMMAND_WORD + PHONE_DESC_GV + EMAIL_DESC_GV + ADDRESS_DESC_GV + THEATER_DESC_THREE;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + EMAIL_DESC_GV + ADDRESS_DESC_GV + THEATER_DESC_THREE;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_GV + ADDRESS_DESC_GV + THEATER_DESC_THREE;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing address -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_GV + EMAIL_DESC_GV + THEATER_DESC_THREE;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: missing theater -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_GV + EMAIL_DESC_GV + ADDRESS_DESC_GV;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "adds " + CinemaUtil.getCinemaDetails(toAdd);
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid name -> rejected */
        command = AddCommand.COMMAND_WORD + INVALID_NAME_DESC + PHONE_DESC_GV
                + EMAIL_DESC_GV + ADDRESS_DESC_GV + THEATER_DESC_THREE;
        assertCommandFailure(command, Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + INVALID_PHONE_DESC
                + EMAIL_DESC_GV + ADDRESS_DESC_GV + THEATER_DESC_THREE;
        assertCommandFailure(command, Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_GV
                + INVALID_EMAIL_DESC + ADDRESS_DESC_GV + THEATER_DESC_THREE;
        assertCommandFailure(command, Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_GV
                + EMAIL_DESC_GV + INVALID_ADDRESS_DESC + THEATER_DESC_THREE;
        assertCommandFailure(command, Address.MESSAGE_ADDRESS_CONSTRAINTS);

        /* Case: invalid theater -> rejected */
        command = AddCommand.COMMAND_WORD + NAME_DESC_GV + PHONE_DESC_GV
                + EMAIL_DESC_GV + ADDRESS_DESC_GV + INVALID_THEATER_DESC;
        assertCommandFailure(command, Theater.MESSAGE_THEATER_CONSTRAINTS);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage} and {@code CinemaListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Cinema toAdd) {
        assertCommandSuccess(CinemaUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Cinema)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(Cinema)
     */
    private void assertCommandSuccess(String command, Cinema toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addCinema(toAdd);
        } catch (DuplicateCinemaException dce) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Cinema)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code CinemaListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddCommandSystemTest#assertCommandSuccess(String, Cinema)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code CinemaListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
