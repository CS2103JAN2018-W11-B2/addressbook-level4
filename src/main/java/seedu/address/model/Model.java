package seedu.address.model;

import java.util.function.Predicate;

import javax.mail.AuthenticationFailedException;

import javafx.collections.ObservableList;
import seedu.address.email.Email;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
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

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Cinema> PREDICATE_SHOW_ALL_CINEMAS = unused -> true;
    Predicate<Movie> PREDICATE_SHOW_ALL_MOVIES = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyMoviePlanner newData);

    /** Returns the MoviePlanner */
    ReadOnlyMoviePlanner getMoviePlanner();

    /** Returns the email Manager Component */
    Email getEmailManager();

    /** Deletes the given cinema. */
    void deleteCinema(Cinema target) throws CinemaNotFoundException;

    /** Adds the given cinema */
    void addCinema(Cinema cinema) throws DuplicateCinemaException;

    /** Deletes {@code tag} from all {@code Cinema}. */
    void deleteTag(Tag tag) throws TagNotFoundException;

    /**
     * Replaces the given cinema {@code target} with {@code editedCinema}.
     *
     * @throws DuplicateCinemaException if updating the cinema's details causes the cinema to be equivalent to
     *      another existing cinema in the list.
     * @throws CinemaNotFoundException if {@code target} could not be found in the list.
     */
    void updateCinema(Cinema target, Cinema editedCinema)
            throws DuplicateCinemaException, CinemaNotFoundException;

    /** Returns an unmodifiable view of the filtered cinema list */
    ObservableList<Cinema> getFilteredCinemaList();

    /** Returns an unmodifiable view of the filtered movie list */
    ObservableList<Movie> getFilteredMovieList();

    /**
     * Updates the filter of the filtered cinema list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredCinemaList(Predicate<Cinema> predicate);

    /** Deletes the given movie. */
    void deleteMovie(Movie target) throws MovieNotFoundException;

    /** Adds the given movie */
    void addMovie(Movie movie) throws DuplicateMovieException;

    /**
     * Replaces the given movie {@code target} with {@code editedMovie}.
     *
     * @throws DuplicateMovieException if updating the cinema's details causes the cinema to be equivalent to
     *      another existing cinema in the list.
     * @throws MovieNotFoundException if {@code target} could not be found in the list.
     */
    void updateMovie(Movie target, Movie editedMovie)
            throws DuplicateMovieException, MovieNotFoundException;

    /**
     * Updates the filter of the filtered movie list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredMovieList(Predicate<Movie> predicate);

    /** Adds the given screening to a theater */
    void addScreening(Screening screening, Theater theater);

    /**
     * Sends email based on input recipient
     *
     * @throws EmailLoginInvalidException if login details is empty
     * @throws EmailMessageEmptyException if message is empty
     * @throws EmailRecipientsEmptyException if recipients list is empty
     * @throws AuthenticationFailedException if gmail account can't be logged in
     */
    void sendEmail(MessageDraft message) throws EmailLoginInvalidException, EmailMessageEmptyException,
            EmailRecipientsEmptyException, AuthenticationFailedException;

    /**
     * Sets login credentials for sending emails
     *
     * @throws EmailLoginInvalidException if login details is invalid
     */
    void loginEmailAccount(String [] loginDetails) throws EmailLoginInvalidException;

    /** Returns Email Sent status **/
    String getEmailStatus();

    /** Clears Email Draft Content **/
    void clearEmailDraft();

    /** Updates Email draft with given message **/
    void draftEmail(MessageDraft message);

}
