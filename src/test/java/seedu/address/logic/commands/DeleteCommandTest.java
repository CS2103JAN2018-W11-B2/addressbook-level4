package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.CommandTestUtil.showCinemaAtIndex;
import static seedu.address.testutil.TypicalCinemas.getTypicalMoviePlanner;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CINEMA;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CINEMA;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.email.EmailManager;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.cinema.Cinema;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalMoviePlanner(), new UserPrefs(), new EmailManager());

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Cinema cinemaToDelete = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_CINEMA);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_CINEMA_SUCCESS, cinemaToDelete);

        ModelManager expectedModel = new ModelManager(model.getMoviePlanner(), new UserPrefs(), new EmailManager());
        expectedModel.deleteCinema(cinemaToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCinemaList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() throws Exception {
        showCinemaAtIndex(model, INDEX_FIRST_CINEMA);

        Cinema cinemaToDelete = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_CINEMA);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_CINEMA_SUCCESS, cinemaToDelete);

        Model expectedModel = new ModelManager(model.getMoviePlanner(), new UserPrefs(), new EmailManager());
        expectedModel.deleteCinema(cinemaToDelete);
        showNoCinema(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showCinemaAtIndex(model, INDEX_FIRST_CINEMA);

        Index outOfBoundIndex = INDEX_SECOND_CINEMA;
        // ensures that outOfBoundIndex is still in bounds of movie planner list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMoviePlanner().getCinemaList().size());

        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Cinema cinemaToDelete = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_CINEMA);
        Model expectedModel = new ModelManager(model.getMoviePlanner(), new UserPrefs(), new EmailManager());

        // delete -> first cinema deleted
        deleteCommand.execute();
        undoRedoStack.push(deleteCommand);

        // undo -> reverts movieplanner back to previous state and filtered cinema list to show all cinemas
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first cinema deleted again
        expectedModel.deleteCinema(cinemaToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCinemaList().size() + 1);
        DeleteCommand deleteCommand = prepareCommand(outOfBoundIndex);

        // execution failed -> deleteCommand not pushed into undoRedoStack
        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code Cinema} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted cinema in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the cinema object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCinemaDeleted() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        DeleteCommand deleteCommand = prepareCommand(INDEX_FIRST_CINEMA);
        Model expectedModel = new ModelManager(model.getMoviePlanner(), new UserPrefs(), new EmailManager());

        showCinemaAtIndex(model, INDEX_SECOND_CINEMA);
        Cinema cinemaToDelete = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        // delete -> deletes second cinema in unfiltered cinema list / first cinema in filtered cinema list
        deleteCommand.execute();
        undoRedoStack.push(deleteCommand);

        // undo -> reverts movieplanner back to previous state and filtered cinema list to show all cinemas
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.deleteCinema(cinemaToDelete);
        assertNotEquals(cinemaToDelete, model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased()));
        // redo -> deletes same second cinema in unfiltered cinema list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        DeleteCommand deleteFirstCommand = prepareCommand(INDEX_FIRST_CINEMA);
        DeleteCommand deleteSecondCommand = prepareCommand(INDEX_SECOND_CINEMA);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = prepareCommand(INDEX_FIRST_CINEMA);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // one command preprocessed when previously equal -> returns false
        deleteFirstCommandCopy.preprocessUndoableCommand();
        assertFalse(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different cinema -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private DeleteCommand prepareCommand(Index index) {
        DeleteCommand deleteCommand = new DeleteCommand(index);
        deleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteCommand;
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoCinema(Model model) {
        model.updateFilteredCinemaList(p -> false);

        assertTrue(model.getFilteredCinemaList().isEmpty());
    }
}
