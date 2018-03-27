package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.DESC_SENGKANG;
import static seedu.address.logic.commands.CommandTestUtil.DESC_TAMPINES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TAMPINES;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_TAMPINES;

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
import seedu.address.logic.commands.EditCommand.EditCinemaDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.MoviePlanner;
import seedu.address.model.UserPrefs;
import seedu.address.model.cinema.Cinema;
import seedu.address.testutil.CinemaBuilder;
import seedu.address.testutil.EditCinemaDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalMoviePlanner(), new UserPrefs(), new EmailManager());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Cinema editedCinema = new CinemaBuilder().build();
        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder(editedCinema).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_CINEMA, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CINEMA_SUCCESS, editedCinema);

        Model expectedModel = new ModelManager(
                new MoviePlanner(model.getMoviePlanner()), new UserPrefs(), new EmailManager());
        expectedModel.updateCinema(model.getFilteredCinemaList().get(0), editedCinema);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastCinema = Index.fromOneBased(model.getFilteredCinemaList().size());
        Cinema lastCinema = model.getFilteredCinemaList().get(indexLastCinema.getZeroBased());

        CinemaBuilder cinemaInList = new CinemaBuilder(lastCinema);
        Cinema editedCinema = cinemaInList.withName(VALID_NAME_TAMPINES).withPhone(VALID_PHONE_TAMPINES)
                .build();

        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder().withName(VALID_NAME_TAMPINES)
                .withPhone(VALID_PHONE_TAMPINES).build();
        EditCommand editCommand = prepareCommand(indexLastCinema, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CINEMA_SUCCESS, editedCinema);

        Model expectedModel = new ModelManager(new MoviePlanner(model.getMoviePlanner()),
                new UserPrefs(), new EmailManager());
        expectedModel.updateCinema(lastCinema, editedCinema);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = prepareCommand(INDEX_FIRST_CINEMA, new EditCinemaDescriptor());
        Cinema editedCinema = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CINEMA_SUCCESS, editedCinema);

        Model expectedModel = new ModelManager(new MoviePlanner(model.getMoviePlanner()),
                new UserPrefs(), new EmailManager());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showCinemaAtIndex(model, INDEX_FIRST_CINEMA);

        Cinema cinemaInFilteredList = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        Cinema editedCinema = new CinemaBuilder(cinemaInFilteredList).withName(VALID_NAME_TAMPINES).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_CINEMA,
                new EditCinemaDescriptorBuilder().withName(VALID_NAME_TAMPINES).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CINEMA_SUCCESS, editedCinema);

        Model expectedModel = new ModelManager(new MoviePlanner(model.getMoviePlanner()),
                new UserPrefs(), new EmailManager());
        expectedModel.updateCinema(model.getFilteredCinemaList().get(0), editedCinema);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateCinemaUnfilteredList_failure() {
        Cinema firstCinema = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder(firstCinema).build();
        EditCommand editCommand = prepareCommand(INDEX_SECOND_CINEMA, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_CINEMA);
    }

    @Test
    public void execute_duplicateCinemaFilteredList_failure() {
        showCinemaAtIndex(model, INDEX_FIRST_CINEMA);

        // edit cinema in filtered list into a duplicate in movie planner
        Cinema cinemaInList = model.getMoviePlanner().getCinemaList().get(INDEX_SECOND_CINEMA.getZeroBased());
        EditCommand editCommand = prepareCommand(INDEX_FIRST_CINEMA,
                new EditCinemaDescriptorBuilder(cinemaInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_CINEMA);
    }

    @Test
    public void execute_invalidCinemaIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCinemaList().size() + 1);
        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder().withName(VALID_NAME_TAMPINES).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of movie planner
     */
    @Test
    public void execute_invalidCinemaIndexFilteredList_failure() {
        showCinemaAtIndex(model, INDEX_FIRST_CINEMA);
        Index outOfBoundIndex = INDEX_SECOND_CINEMA;
        // ensures that outOfBoundIndex is still in bounds of movie planner list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getMoviePlanner().getCinemaList().size());

        EditCommand editCommand = prepareCommand(outOfBoundIndex,
                new EditCinemaDescriptorBuilder().withName(VALID_NAME_TAMPINES).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Cinema editedCinema = new CinemaBuilder().build();
        Cinema cinemaToEdit = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder(editedCinema).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_CINEMA, descriptor);
        Model expectedModel = new ModelManager(new MoviePlanner(model.getMoviePlanner()),
                new UserPrefs(), new EmailManager());

        // edit -> first cinema edited
        editCommand.execute();
        undoRedoStack.push(editCommand);

        // undo -> reverts movieplanner back to previous state and filtered cinema list to show all cinemas
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first cinema edited again
        expectedModel.updateCinema(cinemaToEdit, editedCinema);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCinemaList().size() + 1);
        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder().withName(VALID_NAME_TAMPINES).build();
        EditCommand editCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);

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
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameCinemaEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Cinema editedCinema = new CinemaBuilder().build();
        EditCinemaDescriptor descriptor = new EditCinemaDescriptorBuilder(editedCinema).build();
        EditCommand editCommand = prepareCommand(INDEX_FIRST_CINEMA, descriptor);
        Model expectedModel = new ModelManager(new MoviePlanner(model.getMoviePlanner()),
                new UserPrefs(), new EmailManager());

        showCinemaAtIndex(model, INDEX_SECOND_CINEMA);
        Cinema cinemaToEdit = model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased());
        // edit -> edits second cinema in unfiltered cinema list / first cinema in filtered cinema list
        editCommand.execute();
        undoRedoStack.push(editCommand);

        // undo -> reverts movieplanner back to previous state and filtered cinema list to show all cinemas
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateCinema(cinemaToEdit, editedCinema);
        assertNotEquals(model.getFilteredCinemaList().get(INDEX_FIRST_CINEMA.getZeroBased()), cinemaToEdit);
        // redo -> edits same second cinema in unfiltered cinema list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final EditCommand standardCommand = prepareCommand(INDEX_FIRST_CINEMA, DESC_SENGKANG);

        // same values -> returns true
        EditCinemaDescriptor copyDescriptor = new EditCinemaDescriptor(DESC_SENGKANG);
        EditCommand commandWithSameValues = prepareCommand(INDEX_FIRST_CINEMA, copyDescriptor);
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
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_CINEMA, DESC_SENGKANG)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_CINEMA, DESC_TAMPINES)));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditCommand prepareCommand(Index index, EditCinemaDescriptor descriptor) {
        EditCommand editCommand = new EditCommand(index, descriptor);
        editCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editCommand;
    }
}
