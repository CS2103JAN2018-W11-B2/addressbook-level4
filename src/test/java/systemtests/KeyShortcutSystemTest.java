package systemtests;

import static seedu.address.testutil.TypicalCinemas.GV;

import org.junit.Test;

import guitests.GuiRobot;
import javafx.scene.input.KeyCode;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.cinema.Cinema;
import seedu.address.model.cinema.exceptions.DuplicateCinemaException;
import seedu.address.testutil.CinemaUtil;

public class KeyShortcutSystemTest extends MoviePlannerSystemTest {

    protected final GuiRobot guiRobot = new GuiRobot();

    @Test
    public void undo() {
        /* ------------------------ Perform undo operation on the shown unfiltered list ----------------------------- */

        /* Case: add to empty movie planner -> added */
        deleteAllCinemas();

        /* Case: add a cinema without tags to an empty movie planner -> added */
        assertAddCommandSuccess(GV);
        guiRobot.pauseForHuman();

        /* Case: undo adding Amy to the list -> Amy deleted */
        guiRobot.push(KeyCode.CONTROL, KeyCode.Z);
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        guiRobot.pauseForHuman();
        assertShortcutSuccess(expectedResultMessage);
    }

    @Test
    public void redo() {
        /* ------------------------ Perform redo operation on the shown unfiltered list ----------------------------- */

        /* Case: add to empty movie planner -> added */
        deleteAllCinemas();

        /* Case: add a cinema without tags to an empty movie planner -> added */
        assertAddCommandSuccess(GV);
        guiRobot.pauseForHuman();

        /* Case: undo adding Amy to the list -> Amy deleted */
        guiRobot.push(KeyCode.CONTROL, KeyCode.Z);
        guiRobot.pauseForHuman();

        /* Case: redo adding Amy to the list -> Amy added again */
        guiRobot.push(KeyCode.CONTROL, KeyCode.Y);
        String expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        guiRobot.pauseForHuman();
        assertShortcutSuccess(expectedResultMessage);
    }

    @Test
    public void clear() {
        /* ------------------------ Perform redo operation on the shown unfiltered list ----------------------------- */

        /* Case: add to empty movie planner -> added */
        deleteAllCinemas();

        /* Case: add a cinema without tags to an empty movie planner -> added */
        assertAddCommandSuccess(GV);
        guiRobot.pauseForHuman();

        /* Case: clear list through shortcut key -> list of contacts deleted */
        guiRobot.push(KeyCode.ALT, KeyCode.SHIFT, KeyCode.C);
        String expectedResultMessage = ClearCommand.MESSAGE_SUCCESS;
        guiRobot.pauseForHuman();
        assertShortcutSuccess(expectedResultMessage);
    }

    @Test
    public void list() {
        /* ------------------------ Perform redo operation on the shown unfiltered list ----------------------------- */

        /* Case: add to empty movie planner -> added */
        deleteAllCinemas();

        /* Case: add a cinema without tags to an empty movie planner -> added */
        assertAddCommandSuccess(GV);
        guiRobot.pauseForHuman();

        /* Case: clear list through shortcut key -> list of contacts deleted */
        guiRobot.push(KeyCode.CONTROL, KeyCode.L);
        String expectedResultMessage = ListCommand.MESSAGE_SUCCESS;
        guiRobot.pauseForHuman();
        assertShortcutSuccess(expectedResultMessage);
    }

    /**
     * Executes the {@code AddCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddCommand} with the details of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage} and {@code CinemaListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertAddCommandSuccess(Cinema toAdd) {
        assertAddCommandSuccess(CinemaUtil.getAddCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Cinema)}. Executes {@code command}
     * instead.
     * @see AddCommandSystemTest#assertCommandSuccess(Cinema)
     */
    private void assertAddCommandSuccess(String command, Cinema toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addCinema(toAdd);
        } catch (DuplicateCinemaException dce) {
            throw new IllegalArgumentException("toAdd already exists in the model.");
        }
        String expectedResultMessage = String.format(AddCommand.MESSAGE_SUCCESS, toAdd);

        assertAddCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Cinema)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code CinemaListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddCommandSystemTest#assertCommandSuccess(String, Cinema)
     */
    private void assertAddCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes the {@code UndoCommand/RedoCommand} that undoes {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code UndoCommand/RedoCommand}. <br>
     * 4. {@code Model}, {@code Storage} and {@code CinemaListPanel} equal to the corresponding components in
     * the current model after executing {@code UndoCommand/RedoCommand}.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertShortcutSuccess(String expectedResultMessage) {
        Model expectedModel = getModel();
        assertShortcutSuccess(expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertShortcutSuccess(String, Cinema)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code CinemaListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     */
    private void assertShortcutSuccess(Model expectedModel, String expectedResultMessage) {
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }
}
