# slothhy-unused
###### \EditMovieCommand.java
``` java
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
```
###### \EditMovieCommandParser.java
``` java
/**
 * This is not used because editing the duration might mess up the screening timings
 * for those that are already added to the theaters.
 * Parses input arguments and creates a new EditMovieCommand object
 */
public class EditMovieCommandParser implements Parser<EditMovieCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditMovieCommand
     * and returns an EditMovieCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditMovieCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DURATION, PREFIX_RATING,
                        PREFIX_STARTDATE, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditMovieCommand.MESSAGE_USAGE));
        }

        EditMovieDescriptor editMovieDescriptor = new EditMovieDescriptor();
        try {
            ParserUtil.parseMovieName(argMultimap.getValue(PREFIX_NAME)).ifPresent(editMovieDescriptor::setName);
            ParserUtil.parseDuration(argMultimap.getValue(PREFIX_DURATION)).ifPresent(editMovieDescriptor::setDuration);
            ParserUtil.parseRating(argMultimap.getValue(PREFIX_RATING)).ifPresent(editMovieDescriptor::setRating);
            ParserUtil.parseStartDate(argMultimap.getValue(PREFIX_STARTDATE))
                    .ifPresent(editMovieDescriptor::setStartDate);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editMovieDescriptor::setTags);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editMovieDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditMovieCommand.MESSAGE_NOT_EDITED);
        }

        return new EditMovieCommand(index, editMovieDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
```
###### \EditMovieCommandParserTest.java
``` java
/**
 * This is a test for EditMovie
 */
public class EditMovieCommandParserTest {

    private static final String TAG_EMPTY = " " + PREFIX_TAG;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditMovieCommand.MESSAGE_USAGE);

    private EditMovieCommandParser parser = new EditMovieCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_MOVIENAME_INCREDIBLES, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditMovieCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + MOVIENAME_DESC_MARVEL, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + MOVIENAME_DESC_INCREDIBLES, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid movieName
        assertParseFailure(parser, "1" + INVALID_MOVIENAME_DESC, MovieName.MESSAGE_MOVIENAME_CONSTRAINTS);
        // invalid duration
        assertParseFailure(parser, "1" + INVALID_DURATION_DESC, Duration.MESSAGE_DURATION_CONSTRAINTS);
        // invalid rating
        assertParseFailure(parser, "1" + INVALID_RATING_DESC, Rating.MESSAGE_RATING_CONSTRAINTS);
        // invalid startDate
        assertParseFailure(parser, "1" + INVALID_STARTDATE_DESC, StartDate.MESSAGE_STARTDATE_CONSTRAINTS);
        // invalid tag
        assertParseFailure(parser, "1" + INVALID_TAG_DESC, Tag.MESSAGE_TAG_CONSTRAINTS);

        // invalid duration followed by valid email
        assertParseFailure(parser, "1" + INVALID_DURATION_DESC + RATING_DESC_MARVEL,
                Duration.MESSAGE_DURATION_CONSTRAINTS);

        // valid duration followed by invalid duration. The test case for invalid duration followed by valid duration
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + DURATION_DESC_INCREDIBLES + INVALID_DURATION_DESC,
                Duration.MESSAGE_DURATION_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_MOVIENAME_DESC + INVALID_DURATION_DESC
                        + VALID_RATING_INCREDIBLES + VALID_DURATION_INCREDIBLES,
                MovieName.MESSAGE_MOVIENAME_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_MOVIE;
        String userInput = targetIndex.getOneBased() + RATING_DESC_INCREDIBLES
                + DURATION_DESC_MARVEL + STARTDATE_DESC_MARVEL + MOVIENAME_DESC_MARVEL + TAG_DESC_SUPERHERO;

        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withName(VALID_MOVIENAME_MARVEL)
                .withDuration(VALID_DURATION_MARVEL).withRating(VALID_RATING_INCREDIBLES)
                .withStartDate(VALID_STARTDATE_MARVEL).withTags(VALID_TAG_SUPERHERO).build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_MOVIE;
        String userInput = targetIndex.getOneBased() + DURATION_DESC_INCREDIBLES + RATING_DESC_MARVEL;

        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_INCREDIBLES)
                .withRating(VALID_RATING_MARVEL).build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_MOVIE;
        String userInput = targetIndex.getOneBased() + MOVIENAME_DESC_INCREDIBLES;
        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withName(VALID_MOVIENAME_INCREDIBLES).build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // duration
        userInput = targetIndex.getOneBased() + DURATION_DESC_MARVEL;
        descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_MARVEL).build();
        expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // rating
        userInput = targetIndex.getOneBased() + RATING_DESC_MARVEL;
        descriptor = new EditMovieDescriptorBuilder().withRating(VALID_RATING_MARVEL).build();
        expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // startDate
        userInput = targetIndex.getOneBased() + STARTDATE_DESC_MARVEL;
        descriptor = new EditMovieDescriptorBuilder().withStartDate(VALID_STARTDATE_MARVEL).build();
        expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_MOVIE;
        String userInput = targetIndex.getOneBased()  + DURATION_DESC_MARVEL + STARTDATE_DESC_MARVEL
                + RATING_DESC_MARVEL + DURATION_DESC_MARVEL + STARTDATE_DESC_MARVEL + RATING_DESC_MARVEL
                + DURATION_DESC_INCREDIBLES + STARTDATE_DESC_INCREDIBLES + RATING_DESC_INCREDIBLES + TAG_DESC_COMEDY;

        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_INCREDIBLES)
                .withRating(VALID_RATING_INCREDIBLES).withStartDate(VALID_STARTDATE_INCREDIBLES)
                .withTags(VALID_TAG_COMEDY).build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_MOVIE;
        String userInput = targetIndex.getOneBased() + INVALID_DURATION_DESC + DURATION_DESC_INCREDIBLES;
        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_INCREDIBLES)
                .build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + RATING_DESC_INCREDIBLES + INVALID_DURATION_DESC
                + STARTDATE_DESC_INCREDIBLES + DURATION_DESC_INCREDIBLES;
        descriptor = new EditMovieDescriptorBuilder().withDuration(VALID_DURATION_INCREDIBLES)
                .withRating(VALID_RATING_INCREDIBLES).withStartDate(VALID_STARTDATE_INCREDIBLES).build();
        expectedCommand = new EditMovieCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_resetTags_success() {
        Index targetIndex = INDEX_THIRD_MOVIE;
        String userInput = targetIndex.getOneBased() + TAG_EMPTY;

        EditMovieDescriptor descriptor = new EditMovieDescriptorBuilder().withTags().build();
        EditMovieCommand expectedCommand = new EditMovieCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
```
###### \EditMovieDescriptorBuilder.java
``` java
/**
 * This is not used as this is for building the test for EditMovie
 * A utility class to help with building EditMovieDescriptor objects.
 */
public class EditMovieDescriptorBuilder {

    private EditMovieCommand.EditMovieDescriptor descriptor;

    public EditMovieDescriptorBuilder() {
        descriptor = new EditMovieCommand.EditMovieDescriptor();
    }

    public EditMovieDescriptorBuilder(EditMovieDescriptor descriptor) {
        this.descriptor = new EditMovieDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditMovieDescriptor} with fields containing {@code movie}'s details
     */
    public EditMovieDescriptorBuilder(Movie movie) {
        descriptor = new EditMovieDescriptor();
        descriptor.setName(movie.getName());
        descriptor.setDuration(movie.getDuration());
        descriptor.setRating(movie.getRating());
        descriptor.setStartDate(movie.getStartDate());
        descriptor.setTags(movie.getTags());
    }

    /**
     * Sets the {@code MovieName} of the {@code EditMovieDescriptor} that we are building.
     */
    public EditMovieDescriptorBuilder withName(String movieName) {
        descriptor.setName(new MovieName(movieName));
        return this;
    }

    /**
     * Sets the {@code Duration} of the {@code EditMovieDescriptor} that we are building.
     */
    public EditMovieDescriptorBuilder withDuration(String duration) {
        descriptor.setDuration(new Duration(duration));
        return this;
    }

    /**
     * Sets the {@code Rating} of the {@code EditMovieDescriptor} that we are building.
     */
    public EditMovieDescriptorBuilder withRating(String rating) {
        descriptor.setRating(new Rating(rating));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditMovieDescriptor} that we are building.
     */
    public EditMovieDescriptorBuilder withStartDate(String startDate) {
        descriptor.setStartDate(new StartDate(startDate));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditMovieDescriptor}
     * that we are building.
     */
    public EditMovieDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditMovieDescriptor build() {
        return descriptor;
    }
}
```
