package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.UniqueCinemaList;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;
import seedu.address.model.movie.Movie;
import seedu.address.model.movie.UniqueMovieList;
import seedu.address.model.movie.exceptions.DuplicateMovieException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.exceptions.TagNotFoundException;


/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueCinemaList cinemas;
    private final UniqueMovieList movies;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        cinemas = new UniqueCinemaList();
        tags = new UniqueTagList();
        movies = new UniqueMovieList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Cinemas and Tags in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setCinemas(List<Cinema> cinemas) throws DuplicateCinemaException {
        this.cinemas.setCinemas(cinemas);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Cinema> syncedCinemaList = newData.getCinemaList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());

        try {
            setCinemas(syncedCinemaList);
        } catch (DuplicateCinemaException e) {
            throw new AssertionError("AddressBooks should not have duplicate Cinemas");
        }
    }

    //// Cinema-level operations

    /**
     * Adds a Cinema to the address book.
     * Also checks the new Cinema's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the Cinema to point to those in {@link #tags}.
     *
     * @throws DuplicateCinemaException if an equivalent Cinema already exists.
     */
    public void addCinema(Cinema c) throws DuplicateCinemaException {
        Cinema cinema = syncWithMasterTagList(c);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any Cinema
        // in the Cinema list.
        cinemas.add(cinema);
    }

    /**
     * Replaces the given Cinema {@code target} in the list with {@code editedCinema}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedCinema}.
     *
     * @throws DuplicateCinemaException if updating the Cinema's details causes the Cinema to be equivalent to
     *      another existing Cinema in the list.
     * @throws CinemaNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Cinema)
     */
    public void updateCinema(Cinema target, Cinema editedCinema)
            throws DuplicateCinemaException, CinemaNotFoundException {
        requireNonNull(editedCinema);

        Cinema syncedEditedCinema = syncWithMasterTagList(editedCinema);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any Cinema
        // in the Cinema list.
        cinemas.setCinema(target, syncedEditedCinema);
        removeUnusedTags();
    }

    /**
     *  Updates the master tag list to include tags in {@code Cinema} that are not in the list.
     *  @return a copy of this {@code Cinema} such that every tag in this Cinema points to a Tag object in the master
     *  list.
     */
    private Cinema syncWithMasterTagList(Cinema cinema) {
        final UniqueTagList cinemaTags = new UniqueTagList(cinema.getTags());
        tags.mergeFrom(cinemaTags);

        // Create map with values = tag object references in the master list
        // used for checking Cinema tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of Cinema tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        cinemaTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        return new Cinema(
                cinema.getName(), cinema.getPhone(), cinema.getEmail(), cinema.getAddress(), correctTagReferences);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * @throws CinemaNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removeCinema(Cinema key) throws CinemaNotFoundException {
        if (cinemas.remove(key)) {
            return true;
        } else {
            throw new CinemaNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    /**
     * Removes {@code tag} from this {@code AddressBook}
     * @throws TagNotFoundException if the {@code tag} is not found in this {@code AddressBook}.
     */
    public void removeTag(Tag tag) throws TagNotFoundException {
        if (tags.contains(tag)) {
            for (Cinema cinema : cinemas) {
                removeTagFromCinema(tag, cinema);
            }
        } else {
            throw new TagNotFoundException();
        }
    }

    /**
     * Removes {@code tag} from {@code cinema} if tag is found.
     *
     */
    public void removeTagFromCinema(Tag tag, Cinema cinema) {
        Set<Tag> newTags = new HashSet<>(cinema.getTags());

        if (newTags.remove(tag)) {
            Cinema newCinema = new Cinema(cinema.getName(), cinema.getPhone(), cinema.getEmail(),
                                          cinema.getAddress(), newTags);
            try {
                updateCinema(cinema, newCinema);
            } catch (CinemaNotFoundException cnfe) {
                throw new AssertionError("Cinema should not be missing");
            } catch (DuplicateCinemaException dce) {
                throw new AssertionError("Removing tag should not result in duplicate cinemas");
            }
        }
    }

    /**
     * Removes {@code tag} if tag is not used
     *
     */
    public void removeUnusedTags() {
        Set<Tag> tagsOfCinemas = cinemas.asObservableList()
                                        .stream()
                                        .flatMap(cinema -> cinema.getTags().stream())
                                        .collect(Collectors.toSet());
        tags.setTags(tagsOfCinemas);
    }

    /**
     * Adds a Movie to the address book.
     *
     * @throws DuplicateMovieException if an equivalent Cinema already exists.
     */
    public void addMovie(Movie movie) throws DuplicateMovieException {

        movies.add(movie);
    }

    //// util methods

    @Override
    public String toString() {
        return cinemas.asObservableList().size() + " Cinemas, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Cinema> getCinemaList() {
        return cinemas.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.cinemas.equals(((AddressBook) other).cinemas)
                && this.tags.equalsOrderInsensitive(((AddressBook) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(cinemas, tags);
    }
}
