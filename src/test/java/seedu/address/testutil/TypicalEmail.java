package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_LOGIN_ACCOUNT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_MESSAGE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_RECIPIENT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_SUBJECT;

import seedu.address.email.EmailManager;
import seedu.address.email.exceptions.EmailLoginInvalidException;

/**
 * A utility class containing a list of {@code MessageDraft} objects to be used in tests.
 */
public class TypicalEmail {

    // Manually added
    public static final EmailManager EMAIL_MIKE;

    static {
        EmailManager tempEmailManager = null;
        try {
            tempEmailManager = new EmailBuilder()
                    .withMessage("Hello Mike, the interview details...")
                    .withSubject("Re: Internship Interview")
                    .withRecipient("mikeStudent@u.nus.edu")
                    .withLoginAccount(VALID_EMAIL_LOGIN_ACCOUNT)
                    .build();
        } catch (EmailLoginInvalidException e) {
            e.printStackTrace();
        }
        EMAIL_MIKE = tempEmailManager;
    }

    // Manually added - Email MessageDraft's details found in {@code CommandTestUtil}
    public static final EmailManager EMAIL_DRAFT_1;

    static {
        EmailManager tempEmailManager = null;
        try {
            tempEmailManager = new EmailBuilder()
                    .withMessage(VALID_EMAIL_MESSAGE)
                    .withSubject(VALID_EMAIL_SUBJECT)
                    .withRecipient(VALID_EMAIL_RECIPIENT)
                    .withLoginAccount(VALID_EMAIL_LOGIN_ACCOUNT)
                    .build();
        } catch (EmailLoginInvalidException e) {
            e.printStackTrace();
        }
        EMAIL_DRAFT_1 = tempEmailManager;
    }

    private TypicalEmail() {} // prevents instantiation
}
