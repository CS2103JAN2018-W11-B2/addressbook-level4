package seedu.address.model.movie;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Movie in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Movie {

    private final MovieName moviename;

    public Movie(MovieName moviename) {
        requireAllNonNull(moviename);
        this.moviename = moviename;
    }

    public MovieName getName() {
        return moviename;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Movie)) {
            return false;
        }

        Movie otherMovie = (Movie) other;
        return otherMovie.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(moviename);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName());
        return builder.toString();
    }
}
