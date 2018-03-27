package systemtests;

import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_ANOTHER_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_ANOTHER_RECIPIENT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_ANOTHER_SUBJECT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_LOGIN_ACCOUNT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_RECIPIENT;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_SUBJECT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_LOGIN_ACCOUNT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_RECIPIENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SUBJECT;
import static seedu.address.testutil.TypicalEmail.EMAIL_DRAFT_1;

import org.junit.Test;

import guitests.GuiRobot;
import javafx.scene.input.KeyCode;
import seedu.address.email.EmailManager;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.message.MessageDraft;
import seedu.address.logic.commands.EmailCommand;
import seedu.address.model.Model;
import seedu.address.testutil.EmailBuilder;
import seedu.address.testutil.EmailUtil;

public class EmailCommandSystemTest extends MoviePlannerSystemTest {

    @Test
    public void draftMessageWithoutSend() {
        Model model = getModel();
        GuiRobot guiRobot = new GuiRobot();
        guiRobot.push(KeyCode.SHIFT, KeyCode.TAB);
        guiRobot.pauseForHuman();

        /* ------------------------------------- Perform email operations --------------------------------------- */

        EmailManager toEdit = EMAIL_DRAFT_1;

        /* Case: edit the email draft with all fields (without sending) -> drafted */
        String command = "   " + EmailCommand.COMMAND_WORD + "  "
                + EMAIL_DESC_MESSAGE + "  " + EMAIL_DESC_SUBJECT + " "
                + EMAIL_DESC_RECIPIENT + "   " + EMAIL_DESC_LOGIN_ACCOUNT + " ";
        assertCommandSuccess(command, toEdit);
        guiRobot.pauseForHuman();

        /* Case: edit the email draft with all fields same as previous email draft except recipient -> drafted */
        EmailManager tempEmailManager = null;
        try {
            tempEmailManager = new EmailBuilder().withMessage(VALID_EMAIL_MESSAGE)
                    .withSubject(VALID_EMAIL_SUBJECT)
                    .withRecipient("anotherPerson@gmail.com")
                    .withLoginAccount(VALID_EMAIL_LOGIN_ACCOUNT)
                    .build();
        } catch (EmailLoginInvalidException e) {
            e.printStackTrace();
        }
        toEdit = tempEmailManager;
        command = EmailCommand.COMMAND_WORD + EMAIL_DESC_MESSAGE + EMAIL_DESC_SUBJECT
                + EMAIL_DESC_ANOTHER_RECIPIENT + EMAIL_DESC_LOGIN_ACCOUNT;
        assertCommandSuccess(command, toEdit);
        guiRobot.pauseForHuman();

        /* Case: edit the email draft with all fields same as previous email draft except message
         * -> drafted
         */
        toEdit = EMAIL_DRAFT_1;
        try {
            tempEmailManager = new EmailBuilder().withMessage("Hi Mr. Matthews, This is a new email.")
                    .withSubject(VALID_EMAIL_SUBJECT)
                    .withRecipient(VALID_EMAIL_RECIPIENT)
                    .withLoginAccount(VALID_EMAIL_LOGIN_ACCOUNT)
                    .build();
        } catch (EmailLoginInvalidException e) {
            e.printStackTrace();
        }
        toEdit = tempEmailManager;
        command = EmailCommand.COMMAND_WORD + EMAIL_DESC_ANOTHER_MESSAGE
                + EMAIL_DESC_SUBJECT
                + EMAIL_DESC_RECIPIENT + EMAIL_DESC_LOGIN_ACCOUNT;
        assertCommandSuccess(command, toEdit);
        guiRobot.pauseForHuman();

        /* Case: edit the email draft with all fields same as previous email draft except subject
         * -> drafted
         */
        toEdit = EMAIL_DRAFT_1;
        try {
            tempEmailManager = new EmailBuilder().withMessage(VALID_EMAIL_MESSAGE)
                    .withSubject("Scheduled Meeting")
                    .withRecipient(VALID_EMAIL_RECIPIENT)
                    .withLoginAccount(VALID_EMAIL_LOGIN_ACCOUNT)
                    .build();
        } catch (EmailLoginInvalidException e) {
            e.printStackTrace();
        }
        toEdit = tempEmailManager;
        command = EmailCommand.COMMAND_WORD + EMAIL_DESC_MESSAGE + EMAIL_DESC_ANOTHER_SUBJECT
                + EMAIL_DESC_RECIPIENT + EMAIL_DESC_LOGIN_ACCOUNT;
        assertCommandSuccess(command, toEdit);
        guiRobot.pauseForHuman();

        /* Case: clear email draft -> cleared */
        command = EmailCommand.COMMAND_WORD + " " + "func/clear";
        executeCommand(command);
        guiRobot.pauseForHuman();
    }

    /**
     * Executes the {@code EmailCommand} that adds {@code toEdit} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code EmailCommand} with the details of
     * {@code toEdit}.<br>
     * 4. {@code Model}, {@code MessageDraft} and {@code EmailMessagePanel} equal to the corresponding components in
     * the current model added with {@code toEdit}.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(EmailManager toAdd) {
        assertCommandSuccess(EmailUtil.getEmailCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(EmailManager)}. Executes {@code command}
     * instead.
     * @see EmailCommandSystemTest#assertCommandSuccess(EmailManager)
     */
    private void assertCommandSuccess(String command, EmailManager toEdit) {
        Model expectedModel = getModel();

        MessageDraft tempMessageDraft = new MessageDraft();
        tempMessageDraft.setMessage(toEdit.getEmailDraft().getMessage());
        tempMessageDraft.setSubject(toEdit.getEmailDraft().getSubject());
        tempMessageDraft.setRecipients(toEdit.getEmailDraft().getRecipient());

        String loginAccount = EMAIL_DESC_LOGIN_ACCOUNT.substring(5, 39);
        String[] tempLoginAccount = loginAccount.split(":");
        try {
            expectedModel.loginEmailAccount(tempLoginAccount);
        } catch (EmailLoginInvalidException e) {
            e.printStackTrace();
        }
        expectedModel.draftEmail(tempMessageDraft);
        expectedModel.getEmailManager();

        String expectedResultMessage = String.format(EmailCommand.MESSAGE_SUCCESS, expectedModel.getEmailStatus());

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, EmailManager)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code MessageDraft} and {@code EmailMessagePanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see EmailCommandSystemTest#assertCommandSuccess(String, EmailManager)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        //assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code MessageDraft} and {@code CinemaListPanel} remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see MoviePlannerSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
