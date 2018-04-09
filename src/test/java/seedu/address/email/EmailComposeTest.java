package seedu.address.email;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import seedu.address.email.message.MessageDraft;

//@@author chanyikwai
public class EmailComposeTest {

    private EmailCompose emailCompose;

    @Before
    public void setUp() {
        emailCompose = new EmailCompose();
    }

    @Test
    public void composeEmail() {
        MessageDraft validDraft = new MessageDraft();
        final String validMessage = "This is a message body.";
        final String validSubject = "This is a subject.";
        final String validRecipients = "Someone@gmail.com";
        final String validRelativeFilePath = "docs/images/Architecture.png";
        validDraft.setMessage(validMessage);
        validDraft.setSubject(validSubject);
        validDraft.setRecipients(validRecipients);
        validDraft.setRelativeFilePath(validRelativeFilePath);
        emailCompose.composeEmail(validDraft);
        assertEquals(validMessage, emailCompose.getMessage().getMessage());
        assertEquals(validSubject, emailCompose.getMessage().getSubject());
        assertEquals(validRecipients, emailCompose.getMessage().getRecipient());
        assertEquals(validRelativeFilePath, emailCompose.getMessage().getRelativeFilePath());
    }

    @Test
    public void clearEmailDraft() {
        MessageDraft validDraft = new MessageDraft();
        final String validMessage = "This is a message body.";
        final String validSubject = "This is a subject.";
        final String validRecipients = "Someone@gmail.com";
        final String validRelativeFilePath = "docs/images/Architecture.png";
        validDraft.setMessage(validMessage);
        validDraft.setSubject(validSubject);
        validDraft.setRecipients(validRecipients);
        validDraft.setRelativeFilePath(validRelativeFilePath);
        emailCompose.composeEmail(validDraft);
        emailCompose.resetData();
        assertEquals("", emailCompose.getMessage().getMessage());
        assertEquals("", emailCompose.getMessage().getSubject());
        assertEquals("", emailCompose.getMessage().getRecipient());
        assertEquals("", emailCompose.getMessage().getRelativeFilePath());
    }
}
