package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import javax.mail.AuthenticationFailedException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.email.Email;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.MoviePlanner;
import seedu.address.model.ReadOnlyMoviePlanner;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.Theater;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;
import seedu.address.model.movie.Movie;
import seedu.address.model.movie.exceptions.DuplicateMovieException;
import seedu.address.model.movie.exceptions.MovieNotFoundException;
import seedu.address.model.screening.Screening;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;
import seedu.address.testutil.MovieBuilder;

public class AddMovieCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullMovie_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddMovieCommand(null);
    }

    @Test
    public void execute_movieAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingMovieAdded modelStub = new ModelStubAcceptingMovieAdded();
        Movie validMovie = new MovieBuilder().build();

        CommandResult commandResult = getAddMovieCommandForMovie(validMovie, modelStub).execute();

        assertEquals(String.format(AddMovieCommand.MESSAGE_SUCCESS, validMovie), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validMovie), modelStub.moviesAdded);
    }

    @Test
    public void execute_duplicateMovie_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateMovieException();
        Movie validMovie = new MovieBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddMovieCommand.MESSAGE_DUPLICATE_MOVIE);

        getAddMovieCommandForMovie(validMovie, modelStub).execute();
    }

    @Test
    public void equals() {
        Movie incredibles = new MovieBuilder().withMovieName("The Incredibles").build();
        Movie batman = new MovieBuilder().withMovieName("Batman Begins").build();
        AddMovieCommand addIncrediblesCommand = new AddMovieCommand(incredibles);
        AddMovieCommand addBatmanCommand = new AddMovieCommand(batman);

        // same object -> returns true
        assertTrue(addIncrediblesCommand.equals(addIncrediblesCommand));

        // same values -> returns true
        AddMovieCommand addIncrediblesCommandCopy = new AddMovieCommand(incredibles);
        assertTrue(addIncrediblesCommand.equals(addIncrediblesCommandCopy));

        // different types -> returns false
        assertFalse(addIncrediblesCommand.equals(1));

        // null -> returns false
        assertFalse(addIncrediblesCommand.equals(null));

        // different movie -> returns false
        assertFalse(addIncrediblesCommand.equals(addBatmanCommand));
    }

    /**
     * Generates a new AddMovieCommand with the details of the given movie.
     */
    private AddMovieCommand getAddMovieCommandForMovie(Movie movie, Model model) {
        AddMovieCommand command = new AddMovieCommand(movie);
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
        public void addScreening(Screening screening, Theater theater) {
            fail("This method should not be called.");
        }

        @Override
        public Email getEmailManager() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void sendEmail(MessageDraft message) throws EmailLoginInvalidException, EmailMessageEmptyException,
                EmailRecipientsEmptyException, AuthenticationFailedException {
            fail("This method should not be called.");
        }

        @Override
        public void loginEmailAccount(String [] loginDetails) throws EmailLoginInvalidException {
            fail("This method should not be called.");
        }

        @Override
        public String getEmailStatus() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void clearEmailDraft() {
            fail("This method should not be called.");
        }

        @Override
        public void draftEmail(MessageDraft message) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicateMovieException when trying to add a movie.
     */
    private class ModelStubThrowingDuplicateMovieException extends ModelStub {
        @Override
        public void addMovie(Movie movie) throws DuplicateMovieException {
            throw new DuplicateMovieException();
        }

        @Override
        public ReadOnlyMoviePlanner getMoviePlanner() {
            return new MoviePlanner();
        }
    }

    /**
     * A Model stub that always accept the movie being added.
     */
    private class ModelStubAcceptingMovieAdded extends ModelStub {
        final ArrayList<Movie> moviesAdded = new ArrayList<>();

        @Override
        public void addMovie(Movie movie) throws DuplicateMovieException {
            requireNonNull(movie);
            moviesAdded.add(movie);
        }

        @Override
        public ReadOnlyMoviePlanner getMoviePlanner() {
            return new MoviePlanner();
        }
    }

}
