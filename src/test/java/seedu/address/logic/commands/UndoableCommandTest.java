package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import static seedu.address.logic.commands.CommandTestUtil.deleteFirstCinema;
import static seedu.address.logic.commands.CommandTestUtil.showCinemaAtIndex;
import static seedu.address.testutil.TypicalCinemas.getTypicalMoviePlanner;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CINEMA;

import org.junit.Test;

import seedu.address.email.EmailManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;

public class UndoableCommandTest {
    private final Model model = new ModelManager(getTypicalMoviePlanner(), new UserPrefs(), new EmailManager());
    private final DummyCommand dummyCommand = new DummyCommand(model);

    private Model expectedModel = new ModelManager(getTypicalMoviePlanner(), new UserPrefs(), new EmailManager());

    @Test
    public void executeUndo() throws Exception {
        dummyCommand.execute();
        deleteFirstCinema(expectedModel);
        assertEquals(expectedModel, model);

        showCinemaAtIndex(model, INDEX_FIRST_CINEMA);

        // undo() should cause the model's filtered list to show all cinemas
        dummyCommand.undo();
        expectedModel = new ModelManager(getTypicalMoviePlanner(), new UserPrefs(), new EmailManager());
        assertEquals(expectedModel, model);
    }

    @Test
    public void redo() {
        showCinemaAtIndex(model, INDEX_FIRST_CINEMA);

        // redo() should cause the model's filtered list to show all cinemas
        dummyCommand.redo();
        deleteFirstCinema(expectedModel);
        assertEquals(expectedModel, model);
    }

    /**
     * Deletes the first cinema in the model's filtered list.
     */
    class DummyCommand extends UndoableCommand {
        DummyCommand(Model model) {
            this.model = model;
        }

        @Override
        public CommandResult executeUndoableCommand() throws CommandException {
            Cinema cinemaToDelete = model.getFilteredCinemaList().get(0);
            try {
                model.deleteCinema(cinemaToDelete);
            } catch (CinemaNotFoundException cnfe) {
                fail("Impossible: cinemaToDelete was retrieved from model.");
            }
            return new CommandResult("");
        }
    }
}
