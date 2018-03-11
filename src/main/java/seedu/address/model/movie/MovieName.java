package seedu.address.model.movie;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Movie's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class MovieName {

    public static final String MESSAGE_NAME_CONSTRAINTS =
            " names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String movieName;

    /**
     * Constructs a {@code Name}.
     *
     * @param moviename A valid name.
     */
    public MovieName(String moviename) {
        requireNonNull(moviename);
        checkArgument(isValidName(moviename), MESSAGE_NAME_CONSTRAINTS);
        this.movieName = moviename;
    }

    /**
     * Returns true if a given string is a valid cinema name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return movieName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof seedu.address.model.movie.MovieName // instanceof handles nulls
                && this.movieName.equals(((MovieName) other).movieName)); // state check
    }

    @Override
    public int hashCode() {
        return movieName.hashCode();
    }
}
