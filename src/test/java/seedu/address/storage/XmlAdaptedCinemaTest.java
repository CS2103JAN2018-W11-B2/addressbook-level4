package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.storage.XmlAdaptedCinema.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.TypicalCinemas.GV_TIONG_BAHRU;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.cinema.Address;
import seedu.address.model.cinema.Email;
import seedu.address.model.cinema.Name;
import seedu.address.model.cinema.Phone;
import seedu.address.model.cinema.Theater;
import seedu.address.testutil.Assert;

public class XmlAdaptedCinemaTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final int INVALID_THEATER = -5;

    private static final String VALID_NAME = GV_TIONG_BAHRU.getName().toString();
    private static final String VALID_PHONE = GV_TIONG_BAHRU.getPhone().toString();
    private static final String VALID_EMAIL = GV_TIONG_BAHRU.getEmail().toString();
    private static final String VALID_ADDRESS = GV_TIONG_BAHRU.getAddress().toString();
    private static final List<XmlAdaptedTheater> VALID_THEATERLIST = GV_TIONG_BAHRU.getTheaters().stream()
            .map(XmlAdaptedTheater::new)
            .collect(Collectors.toList());
    private static final ArrayList<XmlAdaptedTheater> VALID_THEATER = new ArrayList<>(VALID_THEATERLIST);

    @Test
    public void toModelType_validCinemaDetails_returnsCinema() throws Exception {
        XmlAdaptedCinema cinema = new XmlAdaptedCinema(GV_TIONG_BAHRU);
        assertEquals(GV_TIONG_BAHRU, cinema.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        XmlAdaptedCinema cinema =
                new XmlAdaptedCinema(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_THEATER);
        String expectedMessage = Name.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        XmlAdaptedCinema cinema = new XmlAdaptedCinema(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                                                        VALID_THEATER);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        XmlAdaptedCinema cinema =
                new XmlAdaptedCinema(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_THEATER);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedCinema cinema = new XmlAdaptedCinema(VALID_NAME, null, VALID_EMAIL,
                VALID_ADDRESS, VALID_THEATER);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        XmlAdaptedCinema cinema =
                new XmlAdaptedCinema(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_THEATER);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        XmlAdaptedCinema cinema = new XmlAdaptedCinema(VALID_NAME, VALID_PHONE, null,
                VALID_ADDRESS, VALID_THEATER);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlAdaptedCinema cinema =
                new XmlAdaptedCinema(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_THEATER);
        String expectedMessage = Address.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedCinema cinema = new XmlAdaptedCinema(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_THEATER);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

    @Test
    public void toModelType_invalidTheaterNum_throwsIllegalValueException() {
        String expectedMessage = "";
        if (!Theater.isValidTheater(String.valueOf(INVALID_THEATER))) {
            expectedMessage = Theater.MESSAGE_THEATER_CONSTRAINTS;
        }
        ArrayList<XmlAdaptedTheater> invalidTheater = new ArrayList<>();
        XmlAdaptedCinema cinema = new XmlAdaptedCinema(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, invalidTheater);

        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

    @Test
    public void toModelType_nullTheaterNum_throwsIllegalValueException() {
        XmlAdaptedCinema cinema = new XmlAdaptedCinema(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, null);
        String expectedMessage = Theater.MESSAGE_THEATER_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, cinema::toModelType);
    }

}
