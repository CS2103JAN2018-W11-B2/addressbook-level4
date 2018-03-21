package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.MoviePlanner;
import seedu.address.model.ReadOnlyMoviePlanner;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;
import seedu.address.model.movie.Movie;
import seedu.address.model.movie.exceptions.DuplicateMovieException;
import seedu.address.model.movie.exceptions.MovieNotFoundException;
import seedu.address.model.screening.Screening;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;
import seedu.address.testutil.CinemaBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullCinema_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_cinemaAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingCinemaAdded modelStub = new ModelStubAcceptingCinemaAdded();
        Cinema validCinema = new CinemaBuilder().build();

        CommandResult commandResult = getAddCommandForCinema(validCinema, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validCinema), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validCinema), modelStub.cinemasAdded);
    }

    @Test
    public void execute_duplicateCinema_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateCinemaException();
        Cinema validCinema = new CinemaBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_CINEMA);

        getAddCommandForCinema(validCinema, modelStub).execute();
    }

    @Test
    public void equals() {
        Cinema alice = new CinemaBuilder().withName("Alice").build();
        Cinema shaw = new CinemaBuilder().withName("shaw").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addShawCommand = new AddCommand(shaw);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different cinema -> returns false
        assertFalse(addAliceCommand.equals(addShawCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given cinema.
     */
    private AddCommand getAddCommandForCinema(Cinema cinema, Model model) {
        AddCommand command = new AddCommand(cinema);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addCinema(Cinema cinema) throws DuplicateCinemaException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyMoviePlanner newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyMoviePlanner getMoviePlanner() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deleteCinema(Cinema target) throws CinemaNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateCinema(Cinema target, Cinema editedCinema)
                throws DuplicateCinemaException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag tag) throws TagNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Cinema> getFilteredCinemaList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Movie> getFilteredMovieList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void addMovie(Movie movie) throws DuplicateMovieException {
            fail("This method should not be called.");
        }

        @Override
        public void deleteMovie(Movie target) throws MovieNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updateMovie(Movie target, Movie editedMovie)
                throws DuplicateMovieException {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredCinemaList(Predicate<Cinema> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredMovieList(Predicate<Movie> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void addScreening(Screening screening) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicateCinemaException when trying to add a cinema.
     */
    private class ModelStubThrowingDuplicateCinemaException extends ModelStub {
        @Override
        public void addCinema(Cinema cinema) throws DuplicateCinemaException {
            throw new DuplicateCinemaException();
        }

        @Override
        public ReadOnlyMoviePlanner getMoviePlanner() {
            return new MoviePlanner();
        }
    }

    /**
     * A Model stub that always accept the cinema being added.
     */
    private class ModelStubAcceptingCinemaAdded extends ModelStub {
        final ArrayList<Cinema> cinemasAdded = new ArrayList<>();

        @Override
        public void addCinema(Cinema cinema) throws DuplicateCinemaException {
            requireNonNull(cinema);
            cinemasAdded.add(cinema);
        }

        @Override
        public ReadOnlyMoviePlanner getMoviePlanner() {
            return new MoviePlanner();
        }
    }

}
