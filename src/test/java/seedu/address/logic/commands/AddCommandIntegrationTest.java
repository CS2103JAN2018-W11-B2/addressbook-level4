package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalCinemas.getTypicalMoviePlanner;

import org.junit.Before;
import org.junit.Test;

import seedu.address.email.EmailManager;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.cinema.Cinema;
import seedu.address.testutil.CinemaBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalMoviePlanner(), new UserPrefs(), new EmailManager());
    }

    @Test
    public void execute_newCinema_success() throws Exception {
        Cinema validCinema = new CinemaBuilder().build();

        Model expectedModel = new ModelManager(model.getMoviePlanner(), new UserPrefs(), new EmailManager());
        expectedModel.addCinema(validCinema);

        assertCommandSuccess(prepareCommand(validCinema, model), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validCinema), expectedModel);
    }

    @Test
    public void execute_duplicateCinema_throwsCommandException() {
        Cinema cinemaInList = model.getMoviePlanner().getCinemaList().get(0);
        assertCommandFailure(prepareCommand(cinemaInList, model), model, AddCommand.MESSAGE_DUPLICATE_CINEMA);
    }

    /**
     * Generates a new {@code AddCommand} which upon execution, adds {@code cinema} into the {@code model}.
     */
    private AddCommand prepareCommand(Cinema cinema, Model model) {
        AddCommand command = new AddCommand(cinema);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
