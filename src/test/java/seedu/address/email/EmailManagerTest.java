package seedu.address.email;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.message.MessageDraft;

//@@author chanyikwai
public class EmailManagerTest {
    @Test
    public void equals() {
        EmailManager emailManager = new EmailManager();
        EmailManager emailManagerCopy = new EmailManager();

        // same values -> returns true
        assertTrue(emailManager.equals(emailManager));

        // null -> returns false
        assertFalse(emailManager.equals(null));

        // different types -> returns false
        assertFalse(emailManager.equals(5));

        // different user -> returns false
        try {
            String loginAccount = "example@gmail.com:123";
            String[] loginEmail = loginAccount.split(":");
            emailManagerCopy.loginEmailAccount(loginEmail);
            assertFalse(emailManager.equals(emailManagerCopy));
        } catch (EmailLoginInvalidException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void compose() {
        EmailManager emailManager = new EmailManager();
        MessageDraft messageDraft = new MessageDraft();
        String validMessage = "Message";
        String validSubject = "Subject";
        String validRecipients = "another@gmail.com";
        messageDraft.setMessage(validMessage);
        messageDraft.setSubject(validSubject);
        messageDraft.setRecipients(validRecipients);
        emailManager.composeEmail(messageDraft);

        assertTrue(emailManager.getEmailDraft().getMessage().equals(validMessage));
        assertTrue(emailManager.getEmailDraft().getSubject().equals(validSubject));
        assertTrue(emailManager.getEmailDraft().getRecipient().equals(validRecipients));
    }
}
