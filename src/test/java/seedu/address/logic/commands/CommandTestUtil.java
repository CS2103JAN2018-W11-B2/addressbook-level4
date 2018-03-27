package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_LOGIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_MESSAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_RECIPIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUMOFTHEATERS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.MoviePlanner;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.NameContainsKeywordsPredicate;
import seedu.address.model.cinema.exceptions.CinemaNotFoundException;
import seedu.address.testutil.EditCinemaDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_MOVIENAME_INCREDIBLES = "The Incredibles";
    public static final String VALID_MOVIENAME_MARVEL = "Marvel";
    public static final String VALID_DURATION_INCREDIBLES = "90";
    public static final String VALID_DURATION_MARVEL = "120";
    public static final String VALID_RATING_INCREDIBLES = "PG";
    public static final String VALID_RATING_MARVEL = "NC16";
    public static final String VALID_STARTDATE_INCREDIBLES = "03/03/2000";
    public static final String VALID_STARTDATE_MARVEL = "08/09/2016";
    public static final String VALID_NAME_SENGKANG = "Sengkang Cathay";
    public static final String VALID_NAME_TAMPINES = "Tampines Cathay";
    public static final String VALID_PHONE_SENGKANG = "11111111";
    public static final String VALID_PHONE_TAMPINES = "22222222";
    public static final String VALID_EMAIL_SENGKANG = "sengkang@cathay.com";
    public static final String VALID_EMAIL_TAMPINES = "tampines@cathay.com";
    public static final String VALID_ADDRESS_SENGKANG = "Sengkang Street 1";
    public static final String VALID_ADDRESS_TAMPINES = "Tampines";
    public static final String VALID_TAG_SUPERHERO = "superhero";
    public static final String VALID_TAG_COMEDY = "comedy";
    public static final String VALID_TAG_UNUSED = "unused"; // not to assign to any movie
    public static final String VALID_EMAIL_MESSAGE = "Hello Chief Manager, attached is my email.";
    public static final String VALID_EMAIL_SUBJECT = "Schedule for next month";
    public static final String VALID_EMAIL_RECIPIENT = "chiefCineManager@gmail.com";
    public static final String VALID_EMAIL_LOGIN_ACCOUNT = "cineManager@gmail.com:somePassWord";
    public static final String VALID_ANOTHER_MESSAGE = "Hi Mr. Matthews, This is a new email.";
    public static final String VALID_ANOTHER_SUBJECT = "Scheduled Meeting";
    public static final String VALID_ANOTHER_RECIPIENT = "anotherPerson@gmail.com";
    public static final int VALID_NUMOFTHEATERS = 3;
    public static final int VALID_NUMOFNEWTHEATERS = 5;

    public static final String MOVIENAME_DESC_INCREDIBLES = " " + PREFIX_NAME + VALID_MOVIENAME_INCREDIBLES;
    public static final String MOVIENAME_DESC_MARVEL = " " + PREFIX_NAME + VALID_MOVIENAME_MARVEL;
    public static final String DURATION_DESC_INCREDIBLES = " " + PREFIX_DURATION + VALID_DURATION_INCREDIBLES;
    public static final String DURATION_DESC_MARVEL = " " + PREFIX_DURATION + VALID_DURATION_MARVEL;
    public static final String RATING_DESC_INCREDIBLES = " " + PREFIX_RATING + VALID_RATING_INCREDIBLES;
    public static final String RATING_DESC_MARVEL = " " + PREFIX_RATING + VALID_RATING_MARVEL;
    public static final String STARTDATE_DESC_INCREDIBLES = " " + PREFIX_STARTDATE + VALID_STARTDATE_INCREDIBLES;
    public static final String STARTDATE_DESC_MARVEL = " " + PREFIX_STARTDATE + VALID_STARTDATE_MARVEL;
    public static final String NAME_DESC_SENGKANG = " " + PREFIX_NAME + VALID_NAME_SENGKANG;
    public static final String NAME_DESC_TAMPINES = " " + PREFIX_NAME + VALID_NAME_TAMPINES;
    public static final String PHONE_DESC_SENGKANG = " " + PREFIX_PHONE + VALID_PHONE_SENGKANG;
    public static final String PHONE_DESC_TAMPINES = " " + PREFIX_PHONE + VALID_PHONE_TAMPINES;
    public static final String EMAIL_DESC_SENGKANG = " " + PREFIX_EMAIL + VALID_EMAIL_SENGKANG;
    public static final String EMAIL_DESC_TAMPINES = " " + PREFIX_EMAIL + VALID_EMAIL_TAMPINES;
    public static final String ADDRESS_DESC_SENGKANG = " " + PREFIX_ADDRESS + VALID_ADDRESS_SENGKANG;
    public static final String ADDRESS_DESC_TAMPINES = " " + PREFIX_ADDRESS + VALID_ADDRESS_TAMPINES;
    public static final String TAG_DESC_COMEDY = " " + PREFIX_TAG + VALID_TAG_COMEDY;
    public static final String TAG_DESC_SUPERHERO = " " + PREFIX_TAG + VALID_TAG_SUPERHERO;
    public static final String THEATER_DESC_THREE = " " + PREFIX_NUMOFTHEATERS + VALID_NUMOFTHEATERS;
    public static final String THEATER_DESC_FIVE = " " + PREFIX_NUMOFTHEATERS + VALID_NUMOFNEWTHEATERS;
    public static final String EMAIL_DESC_MESSAGE = " " + PREFIX_EMAIL_MESSAGE + VALID_EMAIL_MESSAGE;
    public static final String EMAIL_DESC_SUBJECT = " " + PREFIX_EMAIL_SUBJECT + VALID_EMAIL_SUBJECT;
    public static final String EMAIL_DESC_RECIPIENT = " " + PREFIX_EMAIL_RECIPIENT + VALID_EMAIL_RECIPIENT;
    public static final String EMAIL_DESC_LOGIN_ACCOUNT = " " + PREFIX_EMAIL_LOGIN + VALID_EMAIL_LOGIN_ACCOUNT;
    public static final String EMAIL_DESC_ANOTHER_MESSAGE = " " + PREFIX_EMAIL_MESSAGE + VALID_ANOTHER_MESSAGE;
    public static final String EMAIL_DESC_ANOTHER_SUBJECT = " " + PREFIX_EMAIL_SUBJECT + VALID_ANOTHER_SUBJECT;
    public static final String EMAIL_DESC_ANOTHER_RECIPIENT = " " + PREFIX_EMAIL_RECIPIENT + VALID_ANOTHER_RECIPIENT;

    public static final String INVALID_MOVIENAME_DESC = " " + PREFIX_NAME + "Ghost&"; // '&' not allowed in movieNames
    public static final String INVALID_DURATION_DESC = " " + PREFIX_DURATION + "12a"; // 'a' not allowed in durations
    public static final String INVALID_RATING_DESC = " " + PREFIX_RATING + "ABCDE"; // rating can only be 2-4 characters
    public static final String INVALID_STARTDATE_DESC = " " + PREFIX_STARTDATE + "ab/ac/abcd"; // invalid date format
    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags
    public static final String INVALID_THEATER_DESC = " "
                                    + PREFIX_NUMOFTHEATERS + "-5"; // negative number not allowed for number of theater

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditCinemaDescriptor DESC_SENGKANG;
    public static final EditCommand.EditCinemaDescriptor DESC_TAMPINES;

    static {
        DESC_SENGKANG = new EditCinemaDescriptorBuilder().withName(VALID_NAME_SENGKANG)
                .withPhone(VALID_PHONE_SENGKANG).withEmail(VALID_EMAIL_SENGKANG).withAddress(VALID_ADDRESS_SENGKANG)
                .withTheaters(VALID_NUMOFTHEATERS).build();
        DESC_TAMPINES = new EditCinemaDescriptorBuilder().withName(VALID_NAME_TAMPINES)
                .withPhone(VALID_PHONE_TAMPINES).withEmail(VALID_EMAIL_TAMPINES).withAddress(VALID_ADDRESS_TAMPINES)
                .withTheaters(VALID_NUMOFTHEATERS).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the movie planner and the filtered cinema list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        MoviePlanner expectedMoviePlanner = new MoviePlanner(actualModel.getMoviePlanner());
        List<Cinema> expectedFilteredList = new ArrayList<>(actualModel.getFilteredCinemaList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedMoviePlanner, actualModel.getMoviePlanner());
            assertEquals(expectedFilteredList, actualModel.getFilteredCinemaList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the cinema at the given {@code targetIndex} in the
     * {@code model}'s movie planner.
     */
    public static void showCinemaAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredCinemaList().size());

        Cinema cinema = model.getFilteredCinemaList().get(targetIndex.getZeroBased());
        final String[] splitName = cinema.getName().fullName.split("\\s+");
        model.updateFilteredCinemaList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredCinemaList().size());
    }

    /**
     * Deletes the first cinema in {@code model}'s filtered list from {@code model}'s movie planner.
     */
    public static void deleteFirstCinema(Model model) {
        Cinema firstCinema = model.getFilteredCinemaList().get(0);
        try {
            model.deleteCinema(firstCinema);
        } catch (CinemaNotFoundException cnfe) {
            throw new AssertionError("Cinema in filtered list must exist in model.", cnfe);
        }
    }

    /**
     * Returns an {@code UndoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static UndoCommand prepareUndoCommand(Model model, UndoRedoStack undoRedoStack) {
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return undoCommand;
    }

    /**
     * Returns a {@code RedoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static RedoCommand prepareRedoCommand(Model model, UndoRedoStack undoRedoStack) {
        RedoCommand redoCommand = new RedoCommand();
        redoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return redoCommand;
    }
}
