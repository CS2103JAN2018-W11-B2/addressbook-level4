package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_LOGIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_MESSAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_RECIPIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_SUBJECT;

import seedu.address.email.EmailManager;
import seedu.address.logic.commands.EmailCommand;

/**
 * A utility class for Cinema.
 */
public class EmailUtil {

    /**
     * Uses the email command word
     * Returns an email command string for updating the {@code messageDraft}.
     */
    public static String getEmailCommand(EmailManager emailManager) {
        return EmailCommand.COMMAND_WORD + " " + getEmailDraftContent(emailManager);
    }

    /**
     * Uses the add command alias
     * Returns an add command string for adding the {@code cinema}.
     */
    public static String getEmailUsingAliasCommand(EmailManager emailManager) {
        return EmailCommand.COMMAND_ALIAS + " " + getEmailDraftContent(emailManager);
    }

    /**
     * Returns the part of command string for the given {@code cinema}'s details.
     */
    public static String getEmailDraftContent(EmailManager emailManager) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_EMAIL_MESSAGE + emailManager.getEmailDraft().getMessage() + " ");
        sb.append(PREFIX_EMAIL_SUBJECT + emailManager.getEmailDraft().getSubject() + " ");
        sb.append(PREFIX_EMAIL_RECIPIENT + emailManager.getEmailDraft().getRecipient() + " ");
        sb.append(PREFIX_EMAIL_LOGIN + "cineManager@gmail.com:somePassword" + " ");
        return sb.toString();
    }
}
