package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_MOVIES;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.movie.Duration;
import seedu.address.model.movie.Movie;
import seedu.address.model.movie.MovieName;
import seedu.address.model.movie.Rating;
import seedu.address.model.movie.StartDate;
import seedu.address.model.movie.exceptions.DuplicateMovieException;
import seedu.address.model.movie.exceptions.MovieNotFoundException;
import seedu.address.model.tag.Tag;
//@@author slothhy-unused
/**
 * This is not used because editing the duration might mess up the screening timings
 * for those that are already added to the theaters.
 * Edits the details of an existing movie in the movie planner.
 */
public class EditMovieCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editmovie";
    public static final String COMMAND_ALIAS = "em";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the movie identified "
            + "by the index number used in the last movie listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_DURATION + "PHONE] "
            + "[" + PREFIX_RATING + "EMAIL] "
            + "[" + PREFIX_STARTDATE + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DURATION + "120 "
            + PREFIX_RATING + "PG "
            + PREFIX_STARTDATE + "20/03/2018 "
            + PREFIX_TAG + "comedy ";

    public static final String MESSAGE_EDIT_MOVIE_SUCCESS = "Edited Movie: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_MOVIE = "This movie already exists in the movie planner.";

    private final Index index;
    private final EditMovieDescriptor editMovieDescriptor;

    private Movie movieToEdit;
    private Movie editedMovie;

    /**
     * @param index of the movie in the filtered movie list to edit
     * @param editMovieDescriptor details to edit the movie with
     */
    public EditMovieCommand(Index index, EditMovieDescriptor editMovieDescriptor) {
        requireNonNull(index);
        requireNonNull(editMovieDescriptor);

        this.index = index;
        this.editMovieDescriptor = new EditMovieDescriptor(editMovieDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateMovie(movieToEdit, editedMovie);
        } catch (DuplicateMovieException dme) {
            throw new CommandException(MESSAGE_DUPLICATE_MOVIE);
        } catch (MovieNotFoundException mnfe) {
            throw new AssertionError("The target movie cannot be missing");
        }
        model.updateFilteredMovieList(PREDICATE_SHOW_ALL_MOVIES);
        return new CommandResult(String.format(MESSAGE_EDIT_MOVIE_SUCCESS, editedMovie));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Movie> lastShownList = model.getFilteredMovieList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MOVIE_DISPLAYED_INDEX);
        }

        movieToEdit = lastShownList.get(index.getZeroBased());
        editedMovie = createEditedMovie(movieToEdit, editMovieDescriptor);
    }

    /**
     * Creates and returns a {@code Movie} with the details of {@code movieToEdit}
     * edited with {@code editMovieDescriptor}.
     */
    private static Movie createEditedMovie(Movie movieToEdit, EditMovieDescriptor editMovieDescriptor) {
        assert movieToEdit != null;

        MovieName updatedMovieName = editMovieDescriptor.getName().orElse(movieToEdit.getName());
        Duration updatedDuration = editMovieDescriptor.getDuration().orElse(movieToEdit.getDuration());
        Rating updatedRating = editMovieDescriptor.getRating().orElse(movieToEdit.getRating());
        StartDate updatedStartDate = editMovieDescriptor.getStartDate().orElse(movieToEdit.getStartDate());
        Set<Tag> updatedTags = editMovieDescriptor.getTags().orElse(movieToEdit.getTags());

        return new Movie(updatedMovieName, updatedDuration, updatedRating, updatedStartDate, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditMovieCommand)) {
            return false;
        }

        // state check
        EditMovieCommand e = (EditMovieCommand) other;
        return index.equals(e.index)
                && editMovieDescriptor.equals(e.editMovieDescriptor)
                && Objects.equals(movieToEdit, e.movieToEdit);
    }

    /**
     * Stores the details to edit the movie with. Each non-empty field value will replace the
     * corresponding field value of the movie.
     */
    public static class EditMovieDescriptor {
        private MovieName movieName;
        private Duration duration;
        private Rating rating;
        private StartDate startDate;
        private Set<Tag> tags;

        public EditMovieDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditMovieDescriptor(EditMovieDescriptor toCopy) {
            setName(toCopy.movieName);
            setDuration(toCopy.duration);
            setRating(toCopy.rating);
            setStartDate(toCopy.startDate);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.movieName, this.duration, this.rating,
                    this.startDate, this.tags);
        }

        public void setName(MovieName movieName) {
            this.movieName = movieName;
        }

        public Optional<MovieName> getName() {
            return Optional.ofNullable(movieName);
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public Optional<Duration> getDuration() {
            return Optional.ofNullable(duration);
        }

        public void setRating(Rating rating) {
            this.rating = rating;
        }

        public Optional<Rating> getRating() {
            return Optional.ofNullable(rating);
        }

        public void setStartDate(StartDate startDate) {
            this.startDate = startDate;
        }

        public Optional<StartDate> getStartDate() {
            return Optional.ofNullable(startDate);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }


        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditMovieDescriptor)) {
                return false;
            }

            // state check
            EditMovieDescriptor e = (EditMovieDescriptor) other;

            return getName().equals(e.getName())
                    && getDuration().equals(e.getDuration())
                    && getRating().equals(e.getRating())
                    && getStartDate().equals(e.getStartDate())
                    && getTags().equals(e.getTags());
        }
    }
}
