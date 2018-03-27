package seedu.address.testutil;

import seedu.address.email.EmailManager;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.message.MessageDraft;

/**
 * A utility class to help with building Email objects.
 */
public class EmailBuilder {

    public static final String DEFAULT_MESSAGE = "Hello Chief Manager. This is the planned schedule.";
    public static final String DEFAULT_SUBJECT = "Planned schedule";
    public static final String DEFAULT_RECIPIENT = "chiefCineManager@gmail.com";
    public static final String DEFAULT_LOGIN_ACCOUNT = "cineManager@gmail.com:somePassword";

    private EmailManager emailManager;
    private MessageDraft messageDraft;
    private String[] loginAccount;

    public EmailBuilder() {
        emailManager = new EmailManager();
        messageDraft = new MessageDraft(DEFAULT_MESSAGE, DEFAULT_SUBJECT, DEFAULT_RECIPIENT);
        loginAccount = DEFAULT_LOGIN_ACCOUNT.split(":");
        emailManager.composeEmail(messageDraft);
        try {
            emailManager.loginEmailAccount(loginAccount);
        } catch (EmailLoginInvalidException e) {
            System.out.println(e);
        }
    }

    /**
     * Initializes the EmailBuilder with the data of {@code messageDraftToCopy}.
     */
    public EmailBuilder(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    /**
     * Sets the {@code message} of the {@code MessageDraft} that we are building.
     */
    public EmailBuilder withMessage(String message) {
        messageDraft.setMessage(message);
        emailManager.composeEmail(messageDraft);
        return this;
    }

    /**
     * Sets the {@code subject} of the {@code MessageDraft} that we are building.
     */
    public EmailBuilder withSubject(String subject) {
        messageDraft.setSubject(subject);
        emailManager.composeEmail(messageDraft);
        return this;
    }

    /**
     * Sets the {@code recipient} of the {@code MessageDraft} that we are building.
     */
    public EmailBuilder withRecipient(String recipient) {
        messageDraft.setRecipients(recipient);
        emailManager.composeEmail(messageDraft);
        return this;
    }

    /**
     * Sets the {@code recipient} of the {@code MessageDraft} that we are building.
     */
    public EmailBuilder withLoginAccount(String loginAccount) throws EmailLoginInvalidException {
        this.loginAccount = loginAccount.split(":");
        emailManager.loginEmailAccount(this.loginAccount);
        return this;
    }

    public EmailManager build() {
        return emailManager;
    }

}
