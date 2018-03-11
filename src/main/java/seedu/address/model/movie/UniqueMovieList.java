package seedu.address.model.movie;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.movie.exceptions.DuplicateMovieException;
import seedu.address.model.movie.exceptions.MovieNotFoundException;

/**
 * A list of movies that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Movie#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueMovieList implements Iterable<Movie> {

    private final ObservableList<Movie> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent movie as the given argument.
     */
    public boolean contains(Movie toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a movie to the list.
     *
     * @throws DuplicateMovieException if the movie to add is a duplicate of an existing movie in the list.
     */
    public void add(Movie toAdd) throws DuplicateMovieException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateMovieException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the movie {@code target} in the list with {@code editedMovie}.
     *
     * @throws DuplicateMovieException if the replacement is equivalent to another existing movie in the list.
     * @throws MovieNotFoundException if {@code target} could not be found in the list.
     */
    public void setMovie(Movie target, Movie editedMovie)
            throws DuplicateMovieException, MovieNotFoundException {
        requireNonNull(editedMovie);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new MovieNotFoundException();
        }

        if (!target.equals(editedMovie) && internalList.contains(editedMovie)) {
            throw new DuplicateMovieException();
        }

        internalList.set(index, editedMovie);
    }

    /**
     * Removes the equivalent movie from the list.
     *
     * @throws MovieNotFoundException if no such movie could be found in the list.
     */
    public boolean remove(Movie toRemove) throws MovieNotFoundException {
        requireNonNull(toRemove);
        final boolean movieFoundAndDeleted = internalList.remove(toRemove);
        if (!movieFoundAndDeleted) {
            throw new MovieNotFoundException();
        }
        return movieFoundAndDeleted;
    }

    public void setMovies(UniqueMovieList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setMovies(List<Movie> movies) throws DuplicateMovieException {
        requireAllNonNull(movies);
        final UniqueMovieList replacement = new UniqueMovieList();
        for (final Movie movie : movies) {
            replacement.add(movie);
        }
        setMovies(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Movie> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Movie> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueMovieList // instanceof handles nulls
                && this.internalList.equals(((UniqueMovieList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
