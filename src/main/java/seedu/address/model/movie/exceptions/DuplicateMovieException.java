package seedu.address.model.movie.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Movie objects.
 */
public class DuplicateMovieException extends DuplicateDataException {
    public DuplicateMovieException() {
        super("Operation would result in duplicate cinemas");
    }
}
