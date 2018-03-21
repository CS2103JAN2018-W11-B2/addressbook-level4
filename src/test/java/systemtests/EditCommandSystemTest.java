package systemtests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_GV;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_SHAW;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_SHAW;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CINEMAS;
import static seedu.address.testutil.TypicalCinemas.GV;
import static seedu.address.testutil.TypicalCinemas.KEYWORD_MATCHING_MEIER;
import static seedu.address.testutil.TypicalCinemas.SHAW;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CINEMA;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.cinema.Address;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.Email;
import seedu.address.model.cinema.Name;
import seedu.address.model.cinema.Phone;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;
import seedu.address.testutil.CinemaBuilder;
import seedu.address.testutil.CinemaUtil;

public class EditCommandSystemTest extends MoviePlannerSystemTest {

    @Test
    public void edit() throws Exception {
        Model model = getModel();

        /* ----------------- Performing edit operation while an unfiltered list is being shown ---------------------- */

        /* Case: edit all fields, command with leading spaces, trailing spaces and multiple spaces between each field
         * -> edited
         */
        Index index = INDEX_FIRST_CINEMA;
        String command = " " + EditCommand.COMMAND_WORD + "  " + index.getOneBased() + "  " + NAME_DESC_SHAW + "  "
                + PHONE_DESC_SHAW + " " + EMAIL_DESC_SHAW + "  " + ADDRESS_DESC_SHAW + " ";
        Cinema editedCinema = new CinemaBuilder().withName(VALID_NAME_SHAW).withPhone(VALID_PHONE_SHAW)
                .withEmail(VALID_EMAIL_SHAW).withAddress(VALID_ADDRESS_SHAW).build();
        assertCommandSuccess(command, index, editedCinema);

        /* Case: undo editing the last cinema in the list -> last cinema restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo editing the last cinema in the list -> last cinema edited again */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        model.updateCinema(
                getModel().getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased()), editedCinema);
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: edit a cinema with new values same as existing values -> edited */
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased()
                + NAME_DESC_SHAW + PHONE_DESC_SHAW + EMAIL_DESC_SHAW + ADDRESS_DESC_SHAW;
        assertCommandSuccess(command, index, SHAW);

        /* ------------------ Performing edit operation while a filtered list is being shown ------------------------ */

        /* Case: filtered cinema list, edit index within bounds of movie planner and cinema list -> edited */
        showCinemasWithName(KEYWORD_MATCHING_MEIER);
        index = INDEX_FIRST_CINEMA;
        assertTrue(index.getZeroBased() < getModel().getFilteredCinemaList().size());
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + " " + NAME_DESC_SHAW;
        Cinema cinemaToEdit = getModel().getFilteredCinemaList().get(index.getZeroBased());
        editedCinema = new CinemaBuilder(cinemaToEdit).withName(VALID_NAME_SHAW).build();
        assertCommandSuccess(command, index, editedCinema);

        /* Case: filtered cinema list, edit index within bounds of movie planner but out of bounds of cinema list
         * -> rejected
         */
        showCinemasWithName(KEYWORD_MATCHING_MEIER);
        int invalidIndex = getModel().getMoviePlanner().getCinemaList().size();
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_SHAW,
                Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);

        /* --------------------- Performing edit operation while a cinema card is selected -------------------------- */

        /* Case: selects first card in the cinema list, edit a cinema -> edited, card selection remains unchanged but
         * browser url changes
         */
        showAllCinemas();
        index = INDEX_FIRST_CINEMA;
        selectCinema(index);
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_GV + PHONE_DESC_GV + EMAIL_DESC_GV
                + ADDRESS_DESC_GV;
        // this can be misleading: card selection actually remains unchanged but the
        // browser's url is updated to reflect the new cinema's name
        assertCommandSuccess(command, index, GV, index);

        /* --------------------------------- Performing invalid edit operation -------------------------------------- */

        /* Case: invalid index (0) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " 0" + NAME_DESC_SHAW,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (-1) -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " -1" + NAME_DESC_SHAW,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: invalid index (size + 1) -> rejected */
        invalidIndex = getModel().getFilteredCinemaList().size() + 1;
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + invalidIndex + NAME_DESC_SHAW,
                Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);

        /* Case: missing index -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + NAME_DESC_SHAW,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));

        /* Case: missing all fields -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CINEMA.getOneBased(),
                EditCommand.MESSAGE_NOT_EDITED);

        /* Case: invalid name -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CINEMA.getOneBased() + INVALID_NAME_DESC,
                Name.MESSAGE_NAME_CONSTRAINTS);

        /* Case: invalid phone -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CINEMA.getOneBased() + INVALID_PHONE_DESC,
                Phone.MESSAGE_PHONE_CONSTRAINTS);

        /* Case: invalid email -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CINEMA.getOneBased() + INVALID_EMAIL_DESC,
                Email.MESSAGE_EMAIL_CONSTRAINTS);

        /* Case: invalid address -> rejected */
        assertCommandFailure(EditCommand.COMMAND_WORD + " " + INDEX_FIRST_CINEMA.getOneBased() + INVALID_ADDRESS_DESC,
                Address.MESSAGE_ADDRESS_CONSTRAINTS);

        /* Case: edit a cinema with new values same as another cinema's values -> rejected */
        executeCommand(CinemaUtil.getAddCommand(SHAW));
        assertTrue(getModel().getMoviePlanner().getCinemaList().contains(SHAW));
        index = INDEX_FIRST_CINEMA;
        assertFalse(getModel().getFilteredCinemaList().get(index.getZeroBased()).equals(SHAW));
        command = EditCommand.COMMAND_WORD + " " + index.getOneBased() + NAME_DESC_SHAW
                + PHONE_DESC_SHAW + EMAIL_DESC_SHAW + ADDRESS_DESC_SHAW;
        assertCommandFailure(command, EditCommand.MESSAGE_DUPLICATE_CINEMA);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Index, Cinema, Index)} except that
     * the browser url and selected card remain unchanged.
     * @param toEdit the index of the current model's filtered list
     * @see EditCommandSystemTest#assertCommandSuccess(String, Index, Cinema, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Cinema editedCinema) {
        assertCommandSuccess(command, toEdit, editedCinema, null);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} and in addition,<br>
     * 1. Asserts that result display box displays the success message of executing {@code EditCommand}.<br>
     * 2. Asserts that the model related components are updated to reflect the cinema at index {@code toEdit} being
     * updated to values specified {@code editedCinema}.<br>
     * @param toEdit the index of the current model's filtered list.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Index toEdit, Cinema editedCinema,
            Index expectedSelectedCardIndex) {
        Model expectedModel = getModel();
        try {
            expectedModel.updateCinema(
                    expectedModel.getFilteredCinemaList().get(toEdit.getZeroBased()), editedCinema);
            expectedModel.updateFilteredCinemaList(PREDICATE_SHOW_ALL_CINEMAS);
        } catch (DuplicateCinemaException | CinemaNotFoundException e) {
            throw new IllegalArgumentException(
                    "editedCinema is a duplicate in expectedModel, or it isn't found in the model.");
        }

        assertCommandSuccess(command, expectedModel,
                String.format(EditCommand.MESSAGE_EDIT_CINEMA_SUCCESS, editedCinema), expectedSelectedCardIndex);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Model, String, Index)} except that the
     * browser url and selected card remain unchanged.
     * @see EditCommandSystemTest#assertCommandSuccess(String, Model, String, Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        assertCommandSuccess(command, expectedModel, expectedResultMessage, null);
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays an empty string.<br>
     * 2. Asserts that the result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to {@code expectedModel}.<br>
     * 4. Asserts that the browser url and selected card update accordingly depending on the card at
     * {@code expectedSelectedCardIndex}.<br>
     * 5. Asserts that the status bar's sync status changes.<br>
     * 6. Asserts that the command box has the default style class.<br>
     * Verifications 1 to 3 are performed by
     * {@code MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     * @see MoviePlannerSystemTest#assertSelectedCardChanged(Index)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage,
            Index expectedSelectedCardIndex) {
        executeCommand(command);
        expectedModel.updateFilteredCinemaList(PREDICATE_SHOW_ALL_CINEMAS);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        if (expectedSelectedCardIndex != null) {
            assertSelectedCardChanged(expectedSelectedCardIndex);
        } else {
            assertSelectedCardUnchanged();
        }
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and in addition,<br>
     * 1. Asserts that the command box displays {@code command}.<br>
     * 2. Asserts that result display box displays {@code expectedResultMessage}.<br>
     * 3. Asserts that the model related components equal to the current model.<br>
     * 4. Asserts that the browser url, selected card and status bar remain unchanged.<br>
     * 5. Asserts that the command box has the error style.<br>
     * Verifications 1 to 3 are performed by
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
