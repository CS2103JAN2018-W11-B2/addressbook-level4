package seedu.address.model.cinema;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;

/**
 * A list of cinemas that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Cinema#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueCinemaList implements Iterable<Cinema> {

    private final ObservableList<Cinema> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent cinema as the given argument.
     */
    public boolean contains(Cinema toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a cinema to the list.
     *
     * @throws DuplicateCinemaException if the cinema to add is a duplicate of an existing cinema in the list.
     */
    public void add(Cinema toAdd) throws DuplicateCinemaException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateCinemaException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the cinema {@code target} in the list with {@code editedCinema}.
     *
     * @throws DuplicateCinemaException if the replacement is equivalent to another existing cinema in the list.
     * @throws CinemaNotFoundException if {@code target} could not be found in the list.
     */
    public void setCinema(Cinema target, Cinema editedCinema)
            throws DuplicateCinemaException, CinemaNotFoundException {
        requireNonNull(editedCinema);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new CinemaNotFoundException();
        }

        if (!target.equals(editedCinema) && internalList.contains(editedCinema)) {
            throw new DuplicateCinemaException();
        }

        internalList.set(index, editedCinema);
    }

    /**
     * Removes the equivalent cinema from the list.
     *
     * @throws CinemaNotFoundException if no such cinema could be found in the list.
     */
    public boolean remove(Cinema toRemove) throws CinemaNotFoundException {
        requireNonNull(toRemove);
        final boolean cinemaFoundAndDeleted = internalList.remove(toRemove);
        if (!cinemaFoundAndDeleted) {
            throw new CinemaNotFoundException();
        }
        return cinemaFoundAndDeleted;
    }

    public void setCinemas(UniqueCinemaList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setCinemas(List<Cinema> cinemas) throws DuplicateCinemaException {
        requireAllNonNull(cinemas);
        final UniqueCinemaList replacement = new UniqueCinemaList();
        for (final Cinema cinema : cinemas) {
            replacement.add(cinema);
        }
        setCinemas(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Cinema> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Cinema> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueCinemaList // instanceof handles nulls
                        && this.internalList.equals(((UniqueCinemaList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
