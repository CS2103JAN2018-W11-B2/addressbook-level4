package seedu.address.model.cinema;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import seedu.address.model.screening.Screening;
//@@author tinyjy94
/**
 * Represents a theater in cinema
 */
public class Theater {

    public static final String MESSAGE_THEATER_CONSTRAINTS = "Theater number given should be a positive integer!";
    public static final String MESSAGE_THEATERSIZE_CONSTRAINTS = " Theater number given should be less than"
            + " or equals to 20.";

    /**
     * Theater number must be positive
     */
    public static final String THEATER_VALIDATION_REGEX = "^[1-9]\\d*$";

    private int theaterNumber;
    private ArrayList<Screening> screeningList;

    public Theater(int theaterNumber) {
        requireNonNull(theaterNumber);
        checkArgument(isValidTheater(String.valueOf(theaterNumber)), MESSAGE_THEATER_CONSTRAINTS);
        this.theaterNumber = theaterNumber;
        this.screeningList = new ArrayList<>();
    }

    /**
     * Returns true if a given string is a valid theater number.
     */
    public static boolean isValidTheater(String test) {
        return test.matches(THEATER_VALIDATION_REGEX);
    }

    public int getTheaterNumber() {
        return theaterNumber;
    }

    public void setTheaterNumber(int theaterNumber) {
        this.theaterNumber = theaterNumber;
    }

    /**
     * Adds a screening to the sorted screening list of the theater
     */
    public void addScreeningToTheater(Screening screening) {
        screeningList.add(screening);
    }

    /**
     * Adds a screening to the sorted screening list of the theater
     */
    public void setScreeningList(ArrayList<Screening> screeningList) {

        this.screeningList = screeningList;
        System.out.println("After adding screening" + screeningList.size());
    }

    /**
     * Sorts the screening list by screening date time
     */
    public void sortScreeningList() {
        screeningList.sort(Comparator.comparing(Screening::getScreeningDateTime));
    }

    /**
     * Returns a list of screenings in the theater
     */
    public ArrayList<Screening> getScreeningList() {
        return screeningList;
    }

    /**
     * Delete a screening given in the theater
     */
    public void deleteScreening(Screening screeningToBeDeleted) {
        screeningList.remove(screeningToBeDeleted);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Theater)) {
            return false;
        }

        Theater otherTheater = (Theater) other;
        return otherTheater.getTheaterNumber() == this.getTheaterNumber();
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(theaterNumber, screeningList);
    }
}
