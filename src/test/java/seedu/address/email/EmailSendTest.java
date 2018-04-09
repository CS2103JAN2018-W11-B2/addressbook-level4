package seedu.address.email;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.mail.AuthenticationFailedException;

import org.junit.Before;
import org.junit.Test;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;

//@@author chanyikwai
public class EmailSendTest {

    private EmailSend emailSend;

    @Before
    public void setUp() {
        emailSend = new EmailSend();
    }

    @Test
    public void sendEmail() throws AuthenticationFailedException {
        EmailCompose emailCompose = new EmailCompose();
        EmailLogin emailLogin = new EmailLogin();

        try {
            emailSend.sendEmail(emailCompose, emailLogin);
        } catch (EmailLoginInvalidException e) {
            assertFalse(emailLogin.isUserLoggedIn());
        } catch (EmailMessageEmptyException e) {
            assertTrue(emailCompose.getMessage().getMessage().isEmpty());
            assertTrue(emailCompose.getMessage().getSubject().isEmpty());

            MessageDraft messageDraft = new MessageDraft();
            messageDraft.setMessage("Message");
            emailCompose.composeEmail(messageDraft);
            assertFalse(emailCompose.getMessage().getMessage().isEmpty());
            assertTrue(emailCompose.getMessage().getSubject().isEmpty());
            emailCompose.resetData();

            messageDraft = new MessageDraft();
            messageDraft.setSubject("Subject");
            emailCompose.composeEmail(messageDraft);
            assertTrue(emailCompose.getMessage().getMessage().isEmpty());
            assertFalse(emailCompose.getMessage().getSubject().isEmpty());
            emailCompose.resetData();
        } catch (EmailRecipientsEmptyException e) {
            MessageDraft messageDraft = new MessageDraft();
            messageDraft.setMessage("Message");
            messageDraft.setSubject("Subject");
            emailCompose.composeEmail(messageDraft);
            assertFalse(emailCompose.getMessage().getMessage().isEmpty());
            assertFalse(emailCompose.getMessage().getSubject().isEmpty());
            assertTrue(emailCompose.getMessage().getRecipient().isEmpty());
            emailCompose.resetData();
        }
    }
}
