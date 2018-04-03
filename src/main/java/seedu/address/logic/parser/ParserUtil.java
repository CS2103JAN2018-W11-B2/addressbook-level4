package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.cinema.Address;
import seedu.address.model.cinema.Email;
import seedu.address.model.cinema.Name;
import seedu.address.model.cinema.Phone;
import seedu.address.model.cinema.Theater;
import seedu.address.model.movie.Duration;
import seedu.address.model.movie.MovieName;
import seedu.address.model.movie.Rating;
import seedu.address.model.movie.StartDate;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 * {@code ParserUtil} contains methods that take in {@code Optional} as parameters. However, it goes against Java's
 * convention (see https://stackoverflow.com/a/39005452) as {@code Optional} should only be used a return type.
 * Justification: The methods in concern receive {@code Optional} return values from other methods as parameters and
 * return {@code Optional} values based on whether the parameters were present. Therefore, it is redundant to unwrap the
 * initial {@code Optional} before passing to {@code ParserUtil} as a parameter and then re-wrap it into an
 * {@code Optional} return value inside {@code ParserUtil} methods.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    private static final int MINUTES_USED_IN_ROUNDING_OFF = 5;
    private static final String DATE_TIME_FORMAT = "dd/MM/uuuu HH:mm";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws IllegalValueException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws IllegalValueException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new IllegalValueException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Name> parseName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(parseName(name.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws IllegalValueException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code Optional<String> phone} into an {@code Optional<Phone>} if {@code phone} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone) throws IllegalValueException {
        requireNonNull(phone);
        return phone.isPresent() ? Optional.of(parsePhone(phone.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws IllegalValueException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code Optional<String> address} into an {@code Optional<Address>} if {@code address} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Address> parseAddress(Optional<String> address) throws IllegalValueException {
        requireNonNull(address);
        return address.isPresent() ? Optional.of(parseAddress(address.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws IllegalValueException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code Optional<String> email} into an {@code Optional<Email>} if {@code email} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Email> parseEmail(Optional<String> email) throws IllegalValueException {
        requireNonNull(email);
        return email.isPresent() ? Optional.of(parseEmail(email.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws IllegalValueException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new IllegalValueException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws IllegalValueException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String movieName} into a {@code MovieName}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code movieName} is invalid.
     */
    public static MovieName parseMovieName(String movieName) throws IllegalValueException {
        requireNonNull(movieName);
        String trimmedMovieName = movieName.trim();
        if (!MovieName.isValidName(trimmedMovieName)) {
            throw new IllegalValueException(MovieName.MESSAGE_MOVIENAME_CONSTRAINTS);
        }
        return new MovieName(trimmedMovieName);
    }

    /**
     * Parses a {@code Optional<String> movieName} into an {@code Optional<MovieName>} if {@code movieName} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<MovieName> parseMovieName(Optional<String> movieName) throws IllegalValueException {
        requireNonNull(movieName);
        return movieName.isPresent() ? Optional.of(parseMovieName(movieName.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String duration} into a {@code Duration}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code duration} is invalid.
     */
    public static Duration parseDuration(String duration) throws IllegalValueException {
        requireNonNull(duration);
        String trimmedDuration = duration.trim();
        if (!Duration.isValidDuration(trimmedDuration)) {
            throw new IllegalValueException(Duration.MESSAGE_DURATION_CONSTRAINTS);
        }
        return new Duration(trimmedDuration);
    }

    /**
     * Parses a {@code Optional<String> duration} into an {@code Optional<Duration>} if {@code duration} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Duration> parseDuration(Optional<String> duration) throws IllegalValueException {
        requireNonNull(duration);
        return duration.isPresent() ? Optional.of(parseDuration(duration.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String rating} into a {@code Rating}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code rating} is invalid.
     */
    public static Rating parseRating(String rating) throws IllegalValueException {
        requireNonNull(rating);
        String trimmedRating = rating.trim();
        if (!Rating.isValidRating(trimmedRating)) {
            throw new IllegalValueException(Rating.MESSAGE_RATING_CONSTRAINTS);
        }
        return new Rating(trimmedRating);
    }

    /**
     * Parses a {@code Optional<String> rating} into an {@code Optional<Rating>} if {@code rating} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Rating> parseRating(Optional<String> rating) throws IllegalValueException {
        requireNonNull(rating);
        return rating.isPresent() ? Optional.of(parseRating(rating.get())) : Optional.empty();
    }

    /**
     * Parses a {@code String startDate} into a {@code StartDate}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code startDate} is invalid.
     */
    public static StartDate parseStartDate(String startDate) throws IllegalValueException {
        requireNonNull(startDate);
        String trimmedStartDate = startDate.trim();
        if (!StartDate.isValidStartDate(trimmedStartDate)) {
            throw new IllegalValueException(StartDate.MESSAGE_STARTDATE_CONSTRAINTS);
        }
        return new StartDate(trimmedStartDate);
    }

    /**
     * Parses a {@code Optional<String> startDate} into an {@code Optional<StartDate>} if {@code startDate} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<StartDate> parseStartDate(Optional<String> startDate) throws IllegalValueException {
        requireNonNull(startDate);
        return startDate.isPresent() ? Optional.of(parseStartDate(startDate.get())) : Optional.empty();
    }
    /**
     * Parses a {@code Optional<String> theaters} into an {@code Optional<ArrayList<Theater>>}
     * if {@code theaters} is present.
     */
    public static Optional<ArrayList<Theater>> parseTheaters(Optional<String> theaters) throws IllegalValueException {
        requireNonNull(theaters);
        return theaters.isPresent() ? Optional.of(parseTheaters(theaters.get())) : Optional.empty();
    }

    /**
     * Parses {@code String theaters} into a {@code ArrayList<Theater>}.
     *
     * @throws IllegalValueException if the given {@code theaters} is invalid.
     */
    public static ArrayList<Theater> parseTheaters(String theaters) throws IllegalValueException {
        requireNonNull(theaters);
        ArrayList<Theater> theaterList = new ArrayList<>();
        if (!Theater.isValidTheater(theaters)) {
            throw new IllegalValueException(Theater.MESSAGE_THEATER_CONSTRAINTS);
        }
        int numOfTheaters = Integer.parseInt(theaters);
        for (int i = 1; i <= numOfTheaters; i++) {
            theaterList.add(new Theater(i));
        }
        return theaterList;
    }

    /**
     * Parses {@code String theater} into a {@code int theaterNumber}.
     * Leading and trailing whitespaces will be trimmed.
     * @throws IllegalValueException if the given {@code String theater} is invalid.
     */
    public static int parseTheaterNumber(String theater) throws IllegalValueException {
        requireNonNull(theater);
        String trimmedTheaterNumber = theater.trim();
        try {
            int theaterNumber = Integer.parseInt(trimmedTheaterNumber);
            if (theaterNumber <= 0) {
                throw new IllegalValueException(Theater.MESSAGE_THEATER_CONSTRAINTS);
            }
            return theaterNumber;
        } catch (NumberFormatException nfe) {
            throw new IllegalValueException(Theater.MESSAGE_THEATER_CONSTRAINTS);
        }
    }

    /**
     * Parses {@code String dateTime} into a {@code LocalDateTime screeningDateTime}.
     * @throws DateTimeParseException if the given {@code String dateTime} is invalid.
     * @throws IllegalValueException if the given Time is not divisible by 5.
     */
    public static LocalDateTime parseScreeningDateTime(String dateTime)
            throws IllegalValueException, DateTimeParseException {
        requireNonNull(dateTime);
        String trimmedDateTime = dateTime.trim();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).withResolverStyle(ResolverStyle.STRICT);
        LocalDateTime screeningDateTime = LocalDateTime.parse(trimmedDateTime, dtf);

        if (screeningDateTime.getMinute() % MINUTES_USED_IN_ROUNDING_OFF != 0) {
            throw new IllegalValueException(Messages.MESSAGE_INVALID_SCREEN_DATE_TIME);
        }
        return screeningDateTime;
    }

    /**
     * Parses a {@code Optional<String> emailMessage} into an {@code String}
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static String parseEmailMessage(Optional<String> emailMessage) throws IllegalValueException {
        requireNonNull(emailMessage);
        return emailMessage.isPresent() ? emailMessage.get() : "";
    }

    /**
     * Parses a {@code Optional<String> emailSubject} into an {@code String}
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static String parseEmailSubject(Optional<String> emailSubject) throws IllegalValueException {
        requireNonNull(emailSubject);
        return emailSubject.isPresent() ? emailSubject.get() : "";
    }

    /**
     * Parses a {@code Optional<String> emailLoginDetails} into an {@code String}
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static String parseEmailLoginDetails(Optional<String> emailLoginDetails) throws IllegalValueException {
        requireNonNull(emailLoginDetails);
        return emailLoginDetails.isPresent() ? emailLoginDetails.get() : "";
    }

    /**
     * Parses a {@code Optional<String> emailTask} into an {@code String}
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static String parseEmailTask(Optional<String> emailTask) throws IllegalValueException {
        requireNonNull(emailTask);
        return emailTask.isPresent() ? emailTask.get() : "";
    }
}
