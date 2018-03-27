package seedu.address.email;

import javax.mail.AuthenticationFailedException;

import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.email.message.ReadOnlyMessageDraft;

/**
 * Email component and relevant API
 */
public interface Email {

    /**
     * Logins to Email Component with given login information
     *
     * @throws EmailLoginInvalidException when login fails
     */
    void loginEmailAccount(String[] loginInformation) throws EmailLoginInvalidException;

    /**
     * Returns true if user is logged in
     */
    boolean isUserLoggedIn();

    /**
     * Creates a draft email template with a given message
     */
    void composeEmail(MessageDraft messageDraft);

    /**
     * Views Email Draft
     */
    ReadOnlyMessageDraft getEmailDraft();

    /**
     * Views the Email Sent Status
     */
    String getEmailStatus();

    /**
     * Clears the Email Draft content
     */
    void clearEmailDraft();

    /**
     * Sends the Email Draft to all users
     *
     * @throws EmailLoginInvalidException if login details is empty
     * @throws EmailMessageEmptyException if message is empty
     * @throws EmailRecipientsEmptyException if recipients list is empty
     * @throws AuthenticationFailedException if gmail account can't be logged in
     */
    void sendEmail() throws EmailLoginInvalidException, EmailMessageEmptyException,
            EmailRecipientsEmptyException, AuthenticationFailedException;
}
