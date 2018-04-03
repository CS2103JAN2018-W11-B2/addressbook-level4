package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CINEMA_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MOVIE_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUMOFTHEATERS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCREENING_DATE_TIME;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ReloadBrowserPanelEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.Theater;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;
import seedu.address.model.movie.Movie;
import seedu.address.model.screening.Screening;

/**
 * Adds a movie screening to a cinema theater.
 */
public class AddScreeningCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addscreening";
    public static final String COMMAND_ALIAS = "as";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a movie screening to a cinema theater. "
            + "Parameters: "
            + PREFIX_MOVIE_INDEX + "MOVIE_INDEX "
            + PREFIX_CINEMA_INDEX + "CINEMA_INDEX "
            + PREFIX_NUMOFTHEATERS + "THEATER_NUMBER "
            + PREFIX_SCREENING_DATE_TIME + "SCREEN_DATE_TIME \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MOVIE_INDEX + "1 "
            + PREFIX_CINEMA_INDEX + "2 "
            + PREFIX_NUMOFTHEATERS + "3 "
            + PREFIX_SCREENING_DATE_TIME + "13/03/2018 13:35";

    public static final String MESSAGE_SUCCESS = "New screening added: %1$s";

    // Constants for calculations
    private static final int PREPARATION_DELAY = 15;
    private static final int MINUTES_USED_IN_ROUNDING_OFF = 5;
    private static final int MINUTES_ENSURE_POSITIVE = 65;

    private final Index movieIndex;
    private final Index cinemaIndex;
    private final int theaterNumber;
    private final LocalDateTime toAddScreeningDateTime;
    private LocalDateTime toAddScreeningEndDateTime;

    private Screening toAdd;
    private Cinema cinema;
    private Cinema updatedCinema;
    private Movie movie;
    private Theater theater;

    /**
     * Creates an AddScreeningCommand to add the specified {@code Screening}
     */
    public AddScreeningCommand(Index movieIndex, Index cinemaIndex,
                               int theaterNumber, LocalDateTime toAddScreeningDateTime) {
        requireNonNull(movieIndex);
        requireNonNull(cinemaIndex);
        requireNonNull(theaterNumber);
        requireNonNull(toAddScreeningDateTime);
        this.movieIndex = movieIndex;
        this.cinemaIndex = cinemaIndex;
        this.theaterNumber = theaterNumber;
        this.toAddScreeningDateTime = toAddScreeningDateTime;
    }

    /**
     * Adds a screening to a cinema and updates the cinema
     * @return CommandResult on successful add screening
     */
    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        try {
            model.updateCinema(cinema, updatedCinema);
            EventsCenter.getInstance().post(new ReloadBrowserPanelEvent(updatedCinema, toAddScreeningEndDateTime));
        } catch (DuplicateCinemaException dce) {
            throw new CommandException(AddCommand.MESSAGE_DUPLICATE_CINEMA);
        } catch (CinemaNotFoundException cnfe) {
            throw new AssertionError("The target cinema cannot be missing");
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    /**
     * Checks that a screening entry is valid and adds it to the updated cinema
     * @throws CommandException if screening is invalid
     */
    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        if (isValidScreening()) {
            String movieName = movie.getName().toString();
            toAdd = new Screening(movieName, theater, toAddScreeningDateTime, toAddScreeningEndDateTime);
            updatedCinema = generateUpdatedCinema(toAdd);
        } else {
            throw new CommandException(Messages.MESSAGE_INVALID_SCREENING);
        }
    }

    /**
     * Creates and returns a {@code Cinema} with the new screening
     */
    private Cinema generateUpdatedCinema(Screening newScreening) {
        ArrayList<Theater> updatedTheaterList = generateUpdatedTheaterList(newScreening);
        return new Cinema(cinema.getName(), cinema.getPhone(), cinema.getEmail(),
                cinema.getAddress(), updatedTheaterList);
    }


    /**
     * Generates and returns an updated list of theaters
     */
    public ArrayList<Theater> generateUpdatedTheaterList(Screening newScreening) {
        ArrayList<Theater> updatedTheaterList = new ArrayList<>();

        for (Theater t : cinema.getTheaters()) {
            // adding screening to this theater
            if (t.equals(theater)) {
                Theater theaterToBeUpdated = new Theater(t.getTheaterNumber());
                ArrayList<Screening> updatedScreeningList = new ArrayList<>();

                // copy existing screenings to new list
                for (Screening s : t.getScreeningList()) {
                    updatedScreeningList.add(s);
                }

                // add the updated screening list to the theater
                updatedScreeningList.add(newScreening);
                theaterToBeUpdated.setScreeningList(updatedScreeningList);
                theaterToBeUpdated.sortScreeningList();
                updatedTheaterList.add(theaterToBeUpdated);
            } else {
                updatedTheaterList.add(t);
            }
        }
        return updatedTheaterList;
    }

    /**
     * Checks that a screening is valid
     * @return true if screening is valid
     * @throws CommandException
     */
    private boolean isValidScreening() throws CommandException {
        movie = getValidMovie();
        cinema = getValidCinema();
        theater = getValidTheater(cinema);
        ArrayList<Screening> screeningList = theater.getScreeningList();
        toAddScreeningEndDateTime = getEndTime();

        return isSlotAvailable(screeningList) && canAddMovie(movie);
    }

    /**
     * Gets a valid movie based on the movie index
     * @return a valid movie based on the movie index
     */
    private Movie getValidMovie() throws CommandException {
        List<Movie> lastShownMovieList = model.getFilteredMovieList();

        if (movieIndex.getZeroBased() >= lastShownMovieList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MOVIE_DISPLAYED_INDEX);
        }

        Movie movie = lastShownMovieList.get(movieIndex.getZeroBased());
        return movie;
    }

    /**
     * Gets a valid cinema based on the cinema index
     * @return a valid cinema based on the cinema index
     */
    private Cinema getValidCinema() throws CommandException {
        List<Cinema> lastShownCinemaList = model.getFilteredCinemaList();

        if (cinemaIndex.getZeroBased() >= lastShownCinemaList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CINEMA_DISPLAYED_INDEX);
        }

        Cinema cinema = lastShownCinemaList.get(cinemaIndex.getZeroBased());
        return cinema;
    }

    /**
     * Gets a valid theater based on the cinema provided
     * @return a valid theater based on the cinema provided and theater number
     */
    private Theater getValidTheater(Cinema cinema) throws CommandException {
        int theaterIndex = theaterNumber - 1;

        if (theaterIndex >= cinema.getTheaters().size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_THEATER_NUMBER);
        }

        Theater theater = cinema.getTheaters().get(theaterIndex);
        return theater;
    }

    /**
     * Checks that the screening date of the movie is on or after the release date of the movie
     * @return true if screening date is
     */
    private boolean canAddMovie(Movie movie) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate toAddDate = toAddScreeningDateTime.toLocalDate();
        LocalDate releaseDate = LocalDate.parse(movie.getStartDate().toString(), dtf);
        return toAddDate.equals(releaseDate) || toAddDate.isAfter(releaseDate);
    }

    /**
     * Checks that the screening can fit into the schedule of the theater.
     * @param screeningList list of screenings
     * @return true if the screening can fit into the schedule
     */
    private boolean isSlotAvailable(ArrayList<Screening> screeningList) {
        int totalScreeningsWithSameDate = getTotalScreeningsWithSameDate(screeningList);

        if (totalScreeningsWithSameDate == 0) {
            return true;
        } else if (totalScreeningsWithSameDate == 1) {
            return hasNoConflictWithOneOtherScreening(screeningList);
        } else {
            return hasNoConflictWithBeforeAndAfter(totalScreeningsWithSameDate, screeningList);
        }
    }

    /**
     * Checks that the screening does not conflict with one other screening
     * @param screeningList list of screenings
     * @return true if no conflict
     */
    private boolean hasNoConflictWithOneOtherScreening(ArrayList<Screening> screeningList) {
        for (Screening s: screeningList) {
            if (isSameScreeningDate(toAddScreeningDateTime, s)) {
                return isScreenTimeOnOrBefore(s) || isScreenTimeOnOrAfter(s);
            }
        }
        return false;
    }

    /**
     * Checks that the screening does not conflict with the screening before or screening after it
     * @param totalScreenings number of screenings with the same date
     * @param screeningList list of screenings
     * @return true if no conflict
     */
    private boolean hasNoConflictWithBeforeAndAfter(int totalScreenings, ArrayList<Screening> screeningList) {
        int count = 0;
        boolean hasNoConflict = false;
        Screening screeningBefore = screeningList.get(0);

        if (isSameScreeningDate(toAddScreeningDateTime, screeningBefore)) {
            count++;
        }

        for (int i = 1; i < screeningList.size(); i++) {
            Screening currentScreening = screeningList.get(i);
            if (hasNoConflict == true) {
                break;
            }

            //first screening
            if (count == 1 && isScreenTimeOnOrBefore(screeningBefore)) {
                return true;
            } else if (isSameScreeningDate(toAddScreeningDateTime, currentScreening)) {
                count++;
                // last screening
                if (count == totalScreenings && isScreenTimeOnOrAfter(currentScreening)) {
                    return true;
                // screening in between
                } else {
                    hasNoConflict = isScreenTimeOnOrAfter(screeningBefore)
                            && isScreenTimeOnOrBefore(currentScreening);
                }
            }
            screeningBefore = currentScreening;
        }
        return hasNoConflict;
    }

    /**
     * Checks that the start time of the screening toAdd is on or after the end time of the screening before it
     * @return true if the start time of the screening is on or after the end time of the screening before it
     */
    private boolean isScreenTimeOnOrAfter(Screening screeningBefore) {
        LocalTime toAddTime = toAddScreeningDateTime.toLocalTime();
        LocalTime screeningBeforeTime = screeningBefore.getScreeningEndDateTime().toLocalTime();
        return toAddTime.isAfter(screeningBeforeTime) || toAddTime.equals(screeningBeforeTime);
    }

    /**
     * Checks that the end time of the screening toAdd is on or before the start time of the screening after it
     * @return true if the end time of the screening is before the start time of the screening after it
     */
    private boolean isScreenTimeOnOrBefore(Screening screeningAfter) {
        LocalTime toAddTime = toAddScreeningEndDateTime.toLocalTime();
        LocalTime screeningAfterTime = screeningAfter.getScreeningDateTime().toLocalTime();
        return toAddTime.isBefore(screeningAfterTime) || toAddTime.equals(screeningAfterTime);
    }

    /**
     * Checks if two screenings have the same date
     * @return true if both screenings have the same date
     */
    private boolean isSameScreeningDate(LocalDateTime screeningDateTime, Screening s2) {
        return screeningDateTime.toLocalDate().equals(s2.getScreeningDateTime().toLocalDate());
    }

    /**
     * Calculates the total number of screenings with the same screening date
     * @param screeningList list of screenings
     * @return total number of screenings
     */
    private int getTotalScreeningsWithSameDate(ArrayList<Screening> screeningList) {
        int count = 0;
        for (Screening s: screeningList) {
            if (isSameScreeningDate(toAddScreeningDateTime, s)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculates the time needed to screen a movie.
     * Elements used in calculations are movie's duration, preparation delay and rounding off to nearest 5 minutes
     * @return endTime time where the screening will end
     */
    private LocalDateTime getEndTime() {
        int movieDuration = Integer.parseInt(movie.getDuration().toString());
        LocalDateTime endTime = toAddScreeningDateTime.plusMinutes(movieDuration).plusMinutes(PREPARATION_DELAY);

        if (endTime.getMinute() % MINUTES_USED_IN_ROUNDING_OFF != 0) {
            LocalDateTime roundedTime = endTime;
            roundedTime = roundedTime.withSecond(0).withNano(0).plusMinutes((
                    MINUTES_ENSURE_POSITIVE - roundedTime.getMinute()) % MINUTES_USED_IN_ROUNDING_OFF);
            return roundedTime;
        }
        return endTime;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddScreeningCommand // instanceof handles nulls
                && toAdd.equals(((AddScreeningCommand) other).toAdd));
    }
}
