package seedu.address.model.movie;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
//@@author slothhy
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
