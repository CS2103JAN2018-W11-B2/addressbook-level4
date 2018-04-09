package seedu.address.model.screening;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import seedu.address.model.cinema.Theater;

//@@author qwlai
/**
 * Represents a movie screening in a cinema theater
 */
public class Screening {

    private final String movieName;
    private Theater theater;
    private final LocalDateTime screeningDateTime;
    private final LocalDateTime screeningEndDateTime;

    public Screening(String movieName, Theater theater, LocalDateTime screeningDateTime,
                     LocalDateTime screeningEndDateTime) {
        this.movieName = movieName;
        this.theater = theater;
        this.screeningDateTime = screeningDateTime;
        this.screeningEndDateTime = screeningEndDateTime;
    }

    public String getMovieName() {
        return movieName;
    }

    public Theater getTheater() {
        return theater;
    }

    public LocalDateTime getScreeningDateTime() {
        return screeningDateTime;
    }

    public LocalDateTime getScreeningEndDateTime() {
        return screeningEndDateTime;
    }

    public void setTheater(Theater t) {
        this.theater = t;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Movie: ")
                .append(getMovieName())
                .append(" Theater: ")
                .append(theater.getTheaterNumber())
                .append(" Date: ")
                .append(screeningDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Screening)) {
            return false;
        }

        Screening otherScreening = (Screening) other;
        return otherScreening.getMovieName().equals(this.getMovieName())
                && otherScreening.getScreeningDateTime().equals(this.getScreeningDateTime())
                && otherScreening.getScreeningEndDateTime().equals(this.getScreeningEndDateTime());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(movieName, theater, screeningDateTime, screeningEndDateTime);
    }
}
