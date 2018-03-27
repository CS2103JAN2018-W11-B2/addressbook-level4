package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showCinemaAtIndex;
import static seedu.address.testutil.TypicalCinemas.getTypicalMoviePlanner;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CINEMA;

import org.junit.Before;
import org.junit.Test;

import seedu.address.email.EmailManager;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;
    private ListCommand listCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalMoviePlanner(), new UserPrefs(), new EmailManager());
        expectedModel = new ModelManager(model.getMoviePlanner(), new UserPrefs(), new EmailManager());

        listCommand = new ListCommand();
        listCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showCinemaAtIndex(model, INDEX_FIRST_CINEMA);
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_SUCCESS, expectedModel);
    }
}
