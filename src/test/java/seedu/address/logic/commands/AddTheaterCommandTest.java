package seedu.address.logic.commands;

//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
//import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
//import static seedu.address.logic.commands.CommandTestUtil.showCinemaAtIndex;
import static seedu.address.testutil.TypicalCinemas.getTypicalMoviePlanner;
//import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CINEMA;
//import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CINEMA;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
//import seedu.address.model.MoviePlanner;
import seedu.address.model.UserPrefs;
//import seedu.address.model.cinema.Cinema;
//import seedu.address.testutil.CinemaBuilder;

public class AddTheaterCommandTest {
    /**
     * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
     * and unit tests for addTheaterCommand.
     */
    private Model model = new ModelManager(getTypicalMoviePlanner(), new UserPrefs());

    @Test
    public void execute_invalidCinemaIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCinemaList().size() + 1);
        AddTheaterCommand addTheaterCommand = prepareCommand(outOfBoundIndex, 3);

        assertCommandFailure(addTheaterCommand, model, Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);
    }
    /**
    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Cinema editedCinema = new CinemaBuilder().build();
        Cinema cinemaToEdit = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        AddTheaterCommand addTheaterCommand = prepareCommand(INDEX_FIRST_CINEMA, 3);
        Model expectedModel = new ModelManager(new MoviePlanner(model.getMoviePlanner()), new UserPrefs());

        // edit -> first cinema edited
        addTheaterCommand.execute();
        undoRedoStack.push(addTheaterCommand);

        // undo -> reverts movieplanner back to previous state and filtered cinema list to show all cinemas
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first cinema edited again
        expectedModel.updateCinema(cinemaToEdit, editedCinema);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
    */
    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCinemaList().size() + 1);
        AddTheaterCommand addTheaterCommand = prepareCommand(outOfBoundIndex, 3);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(addTheaterCommand, model, Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code Cinema} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited cinema in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the cinema object regardless of indexing.
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCinemaEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Cinema editedCinema = new CinemaBuilder().build();

        AddTheaterCommand addTheaterCommand = prepareCommand(INDEX_FIRST_CINEMA, editedCinema.getTheaters().size());
        Model expectedModel = new ModelManager(new MoviePlanner(model.getMoviePlanner()), new UserPrefs());

        showCinemaAtIndex(model, INDEX_SECOND_CINEMA);
        Cinema cinemaToEdit = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        // edit -> edits second cinema in unfiltered cinema list / first cinema in filtered cinema list
        addTheaterCommand.execute();
        undoRedoStack.push(addTheaterCommand);

        // undo -> reverts movieplanner back to previous state and filtered cinema list to show all cinemas
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateCinema(cinemaToEdit, editedCinema);
        assertNotEquals(model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased()), cinemaToEdit);
        // redo -> edits same second cinema in unfiltered cinema list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
    */
    /**
    @Test
    public void equals() throws Exception {
        final AddTheaterCommand standardCommand = prepareCommand(INDEX_FIRST_CINEMA, 3);

        // same values -> returns true
        AddTheaterCommand commandWithSameValues = prepareCommand(INDEX_FIRST_CINEMA, 3);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddTheaterCommand(INDEX_SECOND_CINEMA, 3)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new AddTheaterCommand(INDEX_FIRST_CINEMA, 3)));
    }
    */
    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private AddTheaterCommand prepareCommand(Index index, int theaterNum) {
        AddTheaterCommand addTheaterCommand = new AddTheaterCommand(index, theaterNum);
        addTheaterCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addTheaterCommand;
    }
}
