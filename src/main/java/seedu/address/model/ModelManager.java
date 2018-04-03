package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javax.mail.AuthenticationFailedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.MoviePlannerChangedEvent;
import seedu.address.commons.events.ui.EmailDraftChangedEvent;
import seedu.address.commons.events.ui.ReloadBrowserPanelEvent;
import seedu.address.email.Email;
import seedu.address.email.EmailManager;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;
import seedu.address.model.movie.Movie;
import seedu.address.model.movie.exceptions.DuplicateMovieException;
import seedu.address.model.movie.exceptions.MovieNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * Represents the in-memory model of the movie planner data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Email email;
    private final MoviePlanner moviePlanner;
    private final FilteredList<Cinema> filteredCinemas;
    private final FilteredList<Movie> filteredMovies;

    /**
     * Initializes a ModelManager with the given moviePlanner and userPrefs.
     */
    public ModelManager(ReadOnlyMoviePlanner moviePlanner, UserPrefs userPrefs, Email email) {
        super();
        requireAllNonNull(moviePlanner, userPrefs);

        logger.fine("Initializing with movie planner: " + moviePlanner + " and user prefs " + userPrefs);

        this.moviePlanner = new MoviePlanner(moviePlanner);
        this.email = email;
        filteredCinemas = new FilteredList<>(this.moviePlanner.getCinemaList());
        filteredMovies = new FilteredList<>(this.moviePlanner.getMovieList());
    }

    public ModelManager() {
        this(new MoviePlanner(), new UserPrefs(), new EmailManager());
    }

    @Override
    public void resetData(ReadOnlyMoviePlanner newData) {
        moviePlanner.resetData(newData);
        indicateMoviePlannerChanged();
        raise(new ReloadBrowserPanelEvent(moviePlanner));
    }

    @Override
    public ReadOnlyMoviePlanner getMoviePlanner() {
        return moviePlanner;
    }

    @Override
    public Email getEmailManager() {
        return email;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateMoviePlannerChanged() {
        raise(new MoviePlannerChangedEvent(moviePlanner));
    }

    @Override
    public void loginEmailAccount(String [] emailLoginDetails) throws EmailLoginInvalidException {
        email.loginEmailAccount(emailLoginDetails);
    }

    @Override
    public void sendEmail(MessageDraft messageDraft) throws EmailLoginInvalidException, EmailMessageEmptyException,
            EmailRecipientsEmptyException, AuthenticationFailedException {
        email.composeEmail(messageDraft);
        email.sendEmail();

        raise(new EmailDraftChangedEvent(email.getEmailDraft()));
    }

    @Override
    public String getEmailStatus() {
        return email.getEmailStatus();
    }

    @Override
    public void clearEmailDraft() {
        email.clearEmailDraft();

        raise(new EmailDraftChangedEvent(email.getEmailDraft()));
    }

    @Override
    public void draftEmail(MessageDraft message) {
        email.composeEmail(message);

        raise(new EmailDraftChangedEvent(email.getEmailDraft()));
    }

    @Override
    public synchronized void deleteCinema(Cinema target) throws CinemaNotFoundException {
        moviePlanner.removeCinema(target);
        indicateMoviePlannerChanged();
    }

    @Override
    public synchronized void addCinema(Cinema cinema) throws DuplicateCinemaException {
        moviePlanner.addCinema(cinema);
        updateFilteredCinemaList(PREDICATE_SHOW_ALL_CINEMAS);
        indicateMoviePlannerChanged();
    }

    @Override
    public void updateCinema(Cinema target, Cinema editedCinema)
            throws DuplicateCinemaException, CinemaNotFoundException {
        requireAllNonNull(target, editedCinema);

        moviePlanner.updateCinema(target, editedCinema);
        indicateMoviePlannerChanged();
    }

    @Override
    public void deleteTag(Tag tag) throws TagNotFoundException {
        moviePlanner.removeTag(tag);
    }

    @Override
    public synchronized void deleteMovie(Movie target) throws MovieNotFoundException {
        moviePlanner.removeMovie(target);
        indicateMoviePlannerChanged();
    }

    @Override
    public synchronized void addMovie(Movie movie) throws DuplicateMovieException {
        moviePlanner.addMovie(movie);
        updateFilteredMovieList(PREDICATE_SHOW_ALL_MOVIES);
        indicateMoviePlannerChanged();
    }

    @Override
    public void updateMovie(Movie target, Movie editedMovie)
            throws DuplicateMovieException, MovieNotFoundException {
        requireAllNonNull(target, editedMovie);

        moviePlanner.updateMovie(target, editedMovie);
        indicateMoviePlannerChanged();
    }

    //=========== Filtered Cinema List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Cinema} backed by the internal list of
     * {@code moviePlanner}
     */
    @Override
    public ObservableList<Cinema> getFilteredCinemaList() {
        return FXCollections.unmodifiableObservableList(filteredCinemas);
    }

    @Override
    public void updateFilteredCinemaList(Predicate<Cinema> predicate) {
        requireNonNull(predicate);
        filteredCinemas.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return moviePlanner.equals(other.moviePlanner)
                && filteredCinemas.equals(other.filteredCinemas);
    }

    //=========== Filtered Movie List Accessors =============================================================
    @Override
    public ObservableList<Movie> getFilteredMovieList() {
        return FXCollections.unmodifiableObservableList(filteredMovies);
    }

    @Override
    public void updateFilteredMovieList(Predicate<Movie> predicate) {
        requireNonNull(predicate);
        filteredMovies.setPredicate(predicate);
    }
}
