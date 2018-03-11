package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<Cinema> filteredCinemas;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredCinemas = new FilteredList<>(this.addressBook.getCinemaList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deleteCinema(Cinema target) throws CinemaNotFoundException {
        addressBook.removeCinema(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addCinema(Cinema cinema) throws DuplicateCinemaException {
        addressBook.addCinema(cinema);
        updateFilteredCinemaList(PREDICATE_SHOW_ALL_CINEMAS);
        indicateAddressBookChanged();
    }

    @Override
    public void updateCinema(Cinema target, Cinema editedCinema)
            throws DuplicateCinemaException, CinemaNotFoundException {
        requireAllNonNull(target, editedCinema);

        addressBook.updateCinema(target, editedCinema);
        indicateAddressBookChanged();
    }

    @Override
    public void deleteTag(Tag tag) throws TagNotFoundException {
        addressBook.removeTag(tag);
    }

    //=========== Filtered Cinema List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Cinema} backed by the internal list of
     * {@code addressBook}
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
        return addressBook.equals(other.addressBook)
                && filteredCinemas.equals(other.filteredCinemas);
    }

}
