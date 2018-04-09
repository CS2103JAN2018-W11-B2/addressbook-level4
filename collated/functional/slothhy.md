# slothhy
###### \java\seedu\address\logic\commands\AddMovieCommand.java
``` java
/**
 * Adds a movie to the movie planner.
 */
public class AddMovieCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addmovie";
    public static final String COMMAND_ALIAS = "am";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a movie to the movie planner. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_DURATION + "DURATION "
            + PREFIX_RATING + "RATING "
            + PREFIX_STARTDATE + "STARTDATE "
            + PREFIX_TAG + "TAG "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "The Incredibles "
            + PREFIX_DURATION + "90 "
            + PREFIX_RATING + "PG "
            + PREFIX_STARTDATE + "13/03/2018 "
            + PREFIX_TAG + "comedy";

    public static final String MESSAGE_SUCCESS = "New movie added: %1$s";
    public static final String MESSAGE_DUPLICATE_MOVIE = "This movie already exists in the movie planner";

    private final Movie toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Movie}
     */
    public AddMovieCommand(Movie movie) {
        requireNonNull(movie);
        toAdd = movie;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addMovie(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateMovieException e) {
            throw new CommandException(MESSAGE_DUPLICATE_MOVIE);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddMovieCommand // instanceof handles nulls
                && toAdd.equals(((AddMovieCommand) other).toAdd));
    }
}
```
###### \java\seedu\address\logic\commands\DeleteMovieCommand.java
``` java
/**
 * Deletes a movie identified using it's last displayed index from the movie planner.
 */
public class DeleteMovieCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deletemovie";
    public static final String COMMAND_ALIAS = "dm";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the movie identified by the index number used in the last movie listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_MOVIE_SUCCESS = "Deleted Movie: %1$s";

    private final Index targetIndex;

    private Movie movieToDelete;

    public DeleteMovieCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(movieToDelete);
        try {
            movieToDelete.deleteScreenings();
            model.deleteMovie(movieToDelete);
        } catch (MovieNotFoundException mnfe) {
            throw new AssertionError("The target movie cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_DELETE_MOVIE_SUCCESS, movieToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Movie> lastShownList = model.getFilteredMovieList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MOVIE_DISPLAYED_INDEX);
        }

        movieToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteMovieCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteMovieCommand) other).targetIndex) // state check
                && Objects.equals(this.movieToDelete, ((DeleteMovieCommand) other).movieToDelete));
    }
}
```
###### \java\seedu\address\logic\commands\FindMovieCommand.java
``` java
/**
 * Finds and lists all movies in movie planner whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindMovieCommand extends Command {

    public static final String COMMAND_WORD = "findmovie";
    public static final String COMMAND_ALIAS = "fm";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all movies whose names contain any of "
            + "parameters specified and their specified keywords (case-insensitive).\n"
            + "Parameters: "
            + "[" + PREFIX_NAME + "KEYWORDS] "
            + "[" + PREFIX_STARTDATE + "STARTDATE] "
            + "[" + PREFIX_TAG + "TAG] "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "avenger horror nemo "
            + PREFIX_STARTDATE + "20/10/2015 "
            + PREFIX_TAG + "superhero";

    private final Predicate<Movie> predicate;

    public  FindMovieCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(NameAndStartDateContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(NameAndStartDateAndTagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(NameAndTagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public  FindMovieCommand(StartDateContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(StartDateAndTagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }


    @Override
    public CommandResult execute() {
        model.updateFilteredMovieList(predicate);
        return new CommandResult(getMessageForMovieListShownSummary(model.getFilteredMovieList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindMovieCommand // instanceof handles nulls
                && this.predicate.equals(((FindMovieCommand) other).predicate)); // state check
    }

}
```
###### \java\seedu\address\logic\parser\AddMovieCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddMovieCommand object
 */
public class AddMovieCommandParser implements Parser<AddMovieCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddMovieCommand
     * and returns an AddMovieCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddMovieCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DURATION, PREFIX_RATING, PREFIX_STARTDATE,
                        PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_DURATION, PREFIX_RATING, PREFIX_STARTDATE,
                PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddMovieCommand.MESSAGE_USAGE));
        }

        try {
            MovieName name = ParserUtil.parseMovieName(argMultimap.getValue(PREFIX_NAME).get());
            Duration duration = ParserUtil.parseDuration(argMultimap.getValue(PREFIX_DURATION).get());
            Rating rating = ParserUtil.parseRating(argMultimap.getValue(PREFIX_RATING).get());
            StartDate startDate = ParserUtil.parseStartDate(argMultimap.getValue(PREFIX_STARTDATE).get());
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            Movie movie = new Movie(name, duration, rating, startDate, tagList);

            return new AddMovieCommand(movie);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### \java\seedu\address\logic\parser\DeleteMovieCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteMovieCommandParser implements Parser<DeleteMovieCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteMovieCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteMovieCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteMovieCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\logic\parser\FindMovieCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindMovieCommand object
 */
public class FindMovieCommandParser implements Parser<FindMovieCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindMovieCommand
     * and returns an FindMovieCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindMovieCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_STARTDATE, PREFIX_TAG);

        if ((!arePrefixesPresent(argMultimap, PREFIX_NAME) && !arePrefixesPresent(argMultimap, PREFIX_STARTDATE)
                && !arePrefixesPresent(argMultimap, PREFIX_TAG))
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindMovieCommand.MESSAGE_USAGE));
        }

        String trimmedArgs = args.trim();
        String nameValues;
        String startDateValues;
        String tagValues;
        String[] nameKeywords;
        String[] startDateKeywords;
        String[] tagKeywords;

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindMovieCommand.MESSAGE_USAGE));
        } else {
            if (!argMultimap.getAllValues(PREFIX_NAME).isEmpty()
                    && !argMultimap.getAllValues(PREFIX_STARTDATE).isEmpty()
                    && !argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
                //name, startDate, tag present
                nameValues = argMultimap.getValue(PREFIX_NAME).get();
                startDateValues = argMultimap.getValue(PREFIX_STARTDATE).get();
                tagValues = argMultimap.getValue(PREFIX_TAG).get();
                nameKeywords = nameValues.split("\\s+");
                startDateKeywords = startDateValues.split("\\s+");
                tagKeywords = tagValues.split("\\s+");
                return new FindMovieCommand(new NameAndStartDateAndTagContainsKeywordsPredicate(
                        Arrays.asList(nameKeywords), Arrays.asList(startDateKeywords), Arrays.asList(tagKeywords)));
            } else if (!argMultimap.getAllValues(PREFIX_NAME).isEmpty()
                    && !argMultimap.getAllValues(PREFIX_STARTDATE).isEmpty()) {
                //name and startDate present
                nameValues = argMultimap.getValue(PREFIX_NAME).get();
                startDateValues = argMultimap.getValue(PREFIX_STARTDATE).get();
                nameKeywords = nameValues.split("\\s+");
                startDateKeywords = startDateValues.split("\\s+");
                return new FindMovieCommand(new NameAndStartDateContainsKeywordsPredicate(
                        Arrays.asList(nameKeywords), Arrays.asList(startDateKeywords)));
            } else if (!argMultimap.getAllValues(PREFIX_NAME).isEmpty()
                    && !argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
                //name and tag present
                nameValues = argMultimap.getValue(PREFIX_NAME).get();
                tagValues = argMultimap.getValue(PREFIX_TAG).get();
                nameKeywords = nameValues.split("\\s+");
                tagKeywords = tagValues.split("\\s+");
                return new FindMovieCommand(new NameAndTagContainsKeywordsPredicate(
                        Arrays.asList(nameKeywords), Arrays.asList(tagKeywords)));
            } else if (!argMultimap.getAllValues(PREFIX_STARTDATE).isEmpty()
                    && !argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
                //startDate and tag present
                startDateValues = argMultimap.getValue(PREFIX_STARTDATE).get();
                tagValues = argMultimap.getValue(PREFIX_TAG).get();
                startDateKeywords = startDateValues.split("\\s+");
                tagKeywords = tagValues.split("\\s+");
                return new FindMovieCommand(new StartDateAndTagContainsKeywordsPredicate(
                        Arrays.asList(startDateKeywords), Arrays.asList(tagKeywords)));
            } else if (!argMultimap.getAllValues(PREFIX_NAME).isEmpty()) {
                //name present only
                nameValues = argMultimap.getValue(PREFIX_NAME).get();
                nameKeywords = nameValues.split("\\s+");
                return new FindMovieCommand(new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
            } else if (!argMultimap.getAllValues(PREFIX_STARTDATE).isEmpty()) {
                //startDate present only
                startDateValues = argMultimap.getValue(PREFIX_STARTDATE).get();
                startDateKeywords = startDateValues.split("\\s+");
                return new FindMovieCommand(new StartDateContainsKeywordsPredicate(Arrays.asList(startDateKeywords)));
            } else if (!argMultimap.getAllValues(PREFIX_TAG).isEmpty()) {
                //tag present only
                tagValues = argMultimap.getValue(PREFIX_TAG).get();
                tagKeywords = tagValues.split("\\s+");
                return new FindMovieCommand(new TagContainsKeywordsPredicate(Arrays.asList(tagKeywords)));
            } else {
                throw new ParseException("Wrong format");
            }
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### \java\seedu\address\model\movie\Duration.java
``` java
/**
 * Represents a Movie's duration in the movie planner.
 * Guarantees: immutable; is valid as declared in {@link #isValidDuration(String)}
 */
public class Duration {


    public static final String MESSAGE_DURATION_CONSTRAINTS =
            "Duration can only contain numbers";
    public static final String DURATION_VALIDATION_REGEX = "^[1-9]\\d*$";
    public final String duration;

    /**
     * Constructs a {@code Duration}.
     *
     * @param duration A valid duration.
     */
    public Duration(String duration) {
        requireNonNull(duration);
        checkArgument(isValidDuration(duration), MESSAGE_DURATION_CONSTRAINTS);
        this.duration = duration;
    }

    /**
     * Returns true if a given string is a valid movie duration.
     */
    public static boolean isValidDuration(String test) {
        return test.matches(DURATION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return duration;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Duration // instanceof handles nulls
                && this.duration.equals(((Duration) other).duration)); // state check
    }

    @Override
    public int hashCode() {
        return duration.hashCode();
    }

}
```
###### \java\seedu\address\model\movie\exceptions\DuplicateMovieException.java
``` java
/**
 * Signals that the operation will result in duplicate Movie objects.
 */
public class DuplicateMovieException extends DuplicateDataException {
    public DuplicateMovieException() {
        super("Operation would result in duplicate movies");
    }
}
```
###### \java\seedu\address\model\movie\exceptions\MovieNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified movie.
 */
public class MovieNotFoundException extends Exception {}
```
###### \java\seedu\address\model\movie\Movie.java
``` java
/**
 * Represents a Movie in the movie planner.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Movie {

    private final MovieName movieName;
    private final Duration duration;
    private final Rating rating;
    private final StartDate startDate;
    private final UniqueTagList tags;
    private final ArrayList<Screening> screenings;

    public Movie(MovieName movieName, Duration duration, Rating rating, StartDate startDate, Set<Tag> tags) {
        requireAllNonNull(movieName, duration, rating, startDate);
        this.movieName = movieName;
        this.duration = duration;
        this.rating = rating;
        this.startDate = startDate;
        // protect internal tags from changes in the arg list
        this.tags = new UniqueTagList(tags);
        this.screenings = new ArrayList<>();
    }

    public void addScreening(Screening s) {
        screenings.add(s);
    }

    /**
     * Called by DeleteMovieCommand.
     * It will delete all screenings linked to the movie.
     */
    public void deleteScreenings() {
        for (int i = 0; i < screenings.size(); i++) {
            Screening s = screenings.get(i);
            Theater t = s.getTheater();
            t.deleteScreening(s);
            screenings.remove(i);
        }
    }

    public MovieName getName() {
        return movieName;
    }

    public Duration getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public StartDate getStartDate() {
        return startDate;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
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
        return otherMovie.getName().equals(this.getName())
                && otherMovie.getDuration().equals(this.getDuration())
                && otherMovie.getRating().equals(this.getRating())
                && otherMovie.getStartDate().equals(this.getStartDate());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(movieName, duration, rating, startDate, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Duration: ")
                .append(getDuration())
                .append(" Rating: ")
                .append(getRating())
                .append(" StartDate: ")
                .append(getStartDate())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
}
```
###### \java\seedu\address\model\movie\MovieName.java
``` java
/**
 * Represents a Movie's name in the movie planner.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class MovieName {

    public static final String MESSAGE_MOVIENAME_CONSTRAINTS =
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
     * @param movieName A valid name.
     */
    public MovieName(String movieName) {
        requireNonNull(movieName);
        checkArgument(isValidName(movieName), MESSAGE_MOVIENAME_CONSTRAINTS);
        this.movieName = movieName;
    }

    /**
     * Returns true if a given string is a valid movie name.
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
```
###### \java\seedu\address\model\movie\NameAndStartDateAndTagContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Movie}'s {@code MovieName} matches any of the keywords given.
 */
public class NameAndStartDateAndTagContainsKeywordsPredicate implements Predicate<Movie> {
    private final List<String> nameKeywords;
    private final List<String> startDateKeywords;
    private final List<String> tagKeywords;


    public NameAndStartDateAndTagContainsKeywordsPredicate(List<String> nameKeywords, List<String> startDateKeywords,
                                                    List<String> tagKeywords) {
        this.nameKeywords = nameKeywords;
        this.startDateKeywords = startDateKeywords;
        this.tagKeywords = tagKeywords;
    }

    public boolean containsCaseInsensitive(String s, List<String> l) {
        return l.stream().anyMatch(x -> x.equalsIgnoreCase(s));
    }

    @Override
    public boolean test(Movie movie) {
        ArrayList<String> tags = new ArrayList();
        for (Tag tag : movie.getTags()) {
            tags.add(tag.tagName);
        }
        return nameKeywords.stream().allMatch
                (keyword -> StringUtil.containsWordIgnoreCase(movie.getName().movieName, keyword))
                && startDateKeywords.stream().allMatch
                (keyword -> StringUtil.containsWordIgnoreCase(movie.getStartDate().startDate, keyword))
                && tagKeywords.stream().allMatch
                (keyword -> containsCaseInsensitive(keyword, tags));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameAndStartDateContainsKeywordsPredicate // instanceof handles nulls
                && this.nameKeywords
                .equals(((NameAndStartDateAndTagContainsKeywordsPredicate) other).nameKeywords)
                && this.startDateKeywords
                .equals(((NameAndStartDateAndTagContainsKeywordsPredicate) other).startDateKeywords)
                && this.tagKeywords
                .equals(((NameAndStartDateAndTagContainsKeywordsPredicate) other).tagKeywords)); // state check
    }

}
```
###### \java\seedu\address\model\movie\NameAndStartDateContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Movie}'s {@code MovieName} matches any of the keywords given.
 */
public class NameAndStartDateContainsKeywordsPredicate implements Predicate<Movie> {
    private final List<String> nameKeywords;
    private final List<String> startDateKeywords;


    public NameAndStartDateContainsKeywordsPredicate(List<String> nameKeywords, List<String> startDateKeywords) {
        this.nameKeywords = nameKeywords;
        this.startDateKeywords = startDateKeywords;
    }

    @Override
    public boolean test(Movie movie) {
        return nameKeywords.stream().allMatch
                (keyword -> StringUtil.containsWordIgnoreCase(movie.getName().movieName, keyword))
                && startDateKeywords.stream().allMatch
                (keyword -> StringUtil.containsWordIgnoreCase(movie.getStartDate().startDate, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameAndStartDateContainsKeywordsPredicate // instanceof handles nulls
                && this.nameKeywords.equals(((NameAndStartDateContainsKeywordsPredicate) other).nameKeywords)
                && this.startDateKeywords.equals(((NameAndStartDateContainsKeywordsPredicate) other)
                .startDateKeywords)); // state check
    }

}
```
###### \java\seedu\address\model\movie\NameAndTagContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Movie}'s {@code MovieName} matches any of the keywords given.
 */
public class NameAndTagContainsKeywordsPredicate implements Predicate<Movie> {
    private final List<String> nameKeywords;
    private final List<String> tagKeywords;


    public NameAndTagContainsKeywordsPredicate(List<String> nameKeywords, List<String> tagKeywords) {
        this.nameKeywords = nameKeywords;
        this.tagKeywords = tagKeywords;
    }

    public boolean containsCaseInsensitive(String s, List<String> l) {
        return l.stream().anyMatch(x -> x.equalsIgnoreCase(s));
    }

    @Override
    public boolean test(Movie movie) {
        ArrayList<String> tags = new ArrayList();
        for (Tag tag : movie.getTags()) {
            tags.add(tag.tagName);
        }
        return nameKeywords.stream().allMatch
                (keyword -> StringUtil.containsWordIgnoreCase(movie.getName().movieName, keyword))
                && tagKeywords.stream().allMatch(keyword -> containsCaseInsensitive(keyword, tags));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameAndStartDateContainsKeywordsPredicate // instanceof handles nulls
                && this.nameKeywords.equals(((NameAndTagContainsKeywordsPredicate) other).nameKeywords)
                && this.tagKeywords.equals(((NameAndTagContainsKeywordsPredicate) other).tagKeywords)); // state check
    }

}
```
###### \java\seedu\address\model\movie\Rating.java
``` java
/**
 * Represents a Movie's duration in the movie planner.
 * Guarantees: immutable; is valid as declared in {@link #isValidRating(String)}
 */
public class Rating {


    public static final String MESSAGE_RATING_CONSTRAINTS =
            "Rating can only contain alphanumeric characters from 2 to 4 characters";
    public static final String RATING_VALIDATION_REGEX = "^\\w{1,4}$";
    public final String rating;

    /**
     * Constructs a {@code Rating}.
     *
     * @param rating A valid rating.
     */
    public Rating(String rating) {
        requireNonNull(rating);
        checkArgument(isValidRating(rating), MESSAGE_RATING_CONSTRAINTS);
        this.rating = rating;
    }

    /**
     * Returns true if a given string is a valid movie rating.
     */
    public static boolean isValidRating(String test) {
        return test.matches(RATING_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return rating;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Rating // instanceof handles nulls
                && this.rating.equals(((Rating) other).rating)); // state check
    }

    @Override
    public int hashCode() {
        return rating.hashCode();
    }

}
```
###### \java\seedu\address\model\movie\StartDate.java
``` java
/**
 * Represents a Movie's startDate in the movie planner.
 * Guarantees: immutable; is valid as declared in {@link #isValidStartDate(String)}
 */
public class StartDate {


    public static final String MESSAGE_STARTDATE_CONSTRAINTS =
            "StartDate must be in this format: DD/MM/YYYY";

    private static final String DATE_TIME_FORMAT = "dd/MM/uuuu";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
            .withResolverStyle(ResolverStyle.STRICT);
    public final String startDate;

    /**
     * Constructs a {@code StartDate}.
     *
     * @param startDate A valid startDate.
     */
    public StartDate(String startDate) {
        requireNonNull(startDate);
        checkArgument(isValidStartDate(startDate), MESSAGE_STARTDATE_CONSTRAINTS);
        this.startDate = startDate;
    }

    /**
     * Returns true if a given string is a valid movie startDate.
     */
    public static boolean isValidStartDate(String test) {
        try {
            LocalDate.parse(test, dtf);
            return true;
        } catch (DateTimeParseException dtpe) {
            System.out.println(dtpe);
        }
        return false;
    }

    @Override
    public String toString() {
        return startDate;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StartDate // instanceof handles nulls
                && this.startDate.equals(((StartDate) other).startDate)); // state check
    }

    @Override
    public int hashCode() {
        return startDate.hashCode();
    }

}
```
###### \java\seedu\address\model\movie\StartDateAndTagContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Movie}'s {@code MovieName} matches any of the keywords given.
 */
public class StartDateAndTagContainsKeywordsPredicate implements Predicate<Movie> {
    private final List<String> startDateKeywords;
    private final List<String> tagKeywords;


    public StartDateAndTagContainsKeywordsPredicate(List<String> startDateKeywords, List<String> tagKeywords) {
        this.startDateKeywords = startDateKeywords;
        this.tagKeywords = tagKeywords;
    }

    public boolean containsCaseInsensitive(String s, List<String> l) {
        return l.stream().anyMatch(x -> x.equalsIgnoreCase(s));
    }

    @Override
    public boolean test(Movie movie) {
        ArrayList<String> tags = new ArrayList();
        for (Tag tag : movie.getTags()) {
            tags.add(tag.tagName);
        }
        return startDateKeywords.stream().allMatch
                (keyword -> StringUtil.containsWordIgnoreCase(movie.getStartDate().startDate, keyword))
                && tagKeywords.stream().allMatch(keyword -> containsCaseInsensitive(keyword, tags));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NameAndStartDateContainsKeywordsPredicate // instanceof handles nulls
                && this.startDateKeywords.equals(((StartDateAndTagContainsKeywordsPredicate) other).startDateKeywords)
                && this.tagKeywords.equals(((StartDateAndTagContainsKeywordsPredicate) other)
                .tagKeywords)); // state check
    }

}
```
###### \java\seedu\address\model\movie\StartDateContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Movie}'s {@code StartDate} matches any of the keywords given.
 */
public class StartDateContainsKeywordsPredicate implements Predicate<Movie> {
    private final List<String> keywords;

    public StartDateContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Movie movie) {
        return keywords.stream()
                .allMatch(keyword -> StringUtil.containsWordIgnoreCase(movie.getStartDate().startDate, keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StartDateContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((StartDateContainsKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### \java\seedu\address\model\movie\TagContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code Movie}'s {@code Tag} matches any of the keywords given.
 */
public class TagContainsKeywordsPredicate implements Predicate<Movie> {
    private final List<String> keywords;

    public TagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    public boolean containsCaseInsensitive(String s, List<String> l) {
        return l.stream().anyMatch(x -> x.equalsIgnoreCase(s));
    }

    @Override
    public boolean test(Movie movie) {
        ArrayList<String> tags = new ArrayList();
        for (Tag tag : movie.getTags()) {
            tags.add(tag.tagName);
        }
        return keywords.stream()
                .allMatch(keyword -> containsCaseInsensitive(keyword, tags));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TagContainsKeywordsPredicate) other).keywords)); // state check
    }

}
```
###### \java\seedu\address\model\movie\UniqueMovieList.java
``` java
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
```
###### \java\seedu\address\storage\XmlAdaptedMovie.java
``` java
/**
 * JAXB-friendly version of the Movie.
 */
public class XmlAdaptedMovie {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Movie's %s field is missing!";

    @XmlElement(required = true)
    private String movieName;
    @XmlElement(required = true)
    private String duration;
    @XmlElement(required = true)
    private String rating;
    @XmlElement(required = true)
    private String startDate;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs an XmlAdaptedMovie.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedMovie() {}

    /**
     * Constructs an {@code XmlAdaptedMovie} with the given movie details.
     */
    public XmlAdaptedMovie(String movieName, String duration, String rating, String startDate,
                           List<XmlAdaptedTag> tagged) {
        this.movieName = movieName;
        this.duration = duration;
        this.rating = rating;
        this.startDate = startDate;
        if (tagged != null) {
            this.tagged = new ArrayList<>(tagged);
        }
    }

    /**
     * Converts a given Movie into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedMovie
     */
    public XmlAdaptedMovie(Movie source) {
        movieName = source.getName().movieName;
        duration = source.getDuration().duration;
        rating = source.getRating().rating;
        startDate = source.getStartDate().startDate;
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Converts this jaxb-friendly adapted movie object into the model's Movie object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted movie
     */
    public Movie toModelType() throws IllegalValueException {
        final List<Tag> movieTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            movieTags.add(tag.toModelType());
        }

        if (this.movieName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    MovieName.class.getSimpleName()));
        }
        if (!MovieName.isValidName(this.movieName)) {
            throw new IllegalValueException(MovieName.MESSAGE_MOVIENAME_CONSTRAINTS);
        }
        final MovieName movieName = new MovieName(this.movieName);

        if (this.duration == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Duration.class.getSimpleName()));
        }
        if (!Duration.isValidDuration(this.duration)) {
            throw new IllegalValueException(Duration.MESSAGE_DURATION_CONSTRAINTS);
        }
        final Duration duration = new Duration(this.duration);

        if (this.rating == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Rating.class.getSimpleName()));
        }
        if (!Rating.isValidRating(this.rating)) {
            throw new IllegalValueException(Rating.MESSAGE_RATING_CONSTRAINTS);
        }
        final Rating rating = new Rating(this.rating);

        if (this.startDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    StartDate.class.getSimpleName()));
        }
        if (!StartDate.isValidStartDate(this.startDate)) {
            throw new IllegalValueException(StartDate.MESSAGE_STARTDATE_CONSTRAINTS);
        }
        final StartDate startDate = new StartDate(this.startDate);

        final Set<Tag> tags = new HashSet<>(movieTags);
        return new Movie(movieName, duration, rating, startDate, tags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedMovie)) {
            return false;
        }

        XmlAdaptedMovie otherMovie = (XmlAdaptedMovie) other;
        return Objects.equals(movieName, otherMovie.movieName)
                && Objects.equals(duration, otherMovie.duration)
                && Objects.equals(rating, otherMovie.rating)
                && Objects.equals(startDate, otherMovie.startDate)
                && tagged.equals(otherMovie.tagged);
    }
}
```
