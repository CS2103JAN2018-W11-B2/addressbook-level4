package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import javax.mail.AuthenticationFailedException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import seedu.address.email.EmailFunction;
import seedu.address.email.exceptions.EmailLoginInvalidException;
import seedu.address.email.exceptions.EmailMessageEmptyException;
import seedu.address.email.exceptions.EmailRecipientsEmptyException;
import seedu.address.email.message.MessageDraft;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Composes an email draft or sends the draft out using gmail account
 */
public class EmailCommand extends Command {

    public static final String COMMAND_WORD = "email";
    public static final String COMMAND_ALIAS = "eml";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Emails all contacts in the last displayed list\n"
            + "Parameters: "
            + "email [ func/<send|clear> ] [ msg/MESSAGE ] [ subj/SUBJECT ] [ lgn/user@gmail.com:password ]"
            + "[ recp/recipient@gmail.com ]\n"
            + "Examples:\n"
            + "1) email msg/This is your sample message.\n"
            + "2) email subj/Sample subject\n"
            + "3) email lgn/test@gmail.com:password\n"
            + "4) email func/<send|clear>\n"
            + "5) email recp/contacts@gv.com\n"
            + "6) email msg/message subj/subject lgn/test@gmail.com:password recp/contacts@gv.com func/send";

    public static final String MESSAGE_SUCCESS = "Email have been %1$s";
    public static final String MESSAGE_LOGIN_INVALID = "You must be logged in with a gmail account to send an email.\n"
            + "Command: email lgn/<username@gmail.com>:<password>";
    public static final String MESSAGE_EMPTY_INVALID = "Your message and subject fields must not be empty when "
            + "sending an email.\n"
            + "Command: email msg/<messages> sub/<subjects>";
    public static final String MESSAGE_RECIPIENT_INVALID = "You must have at least 1 recipient to send the email to.";
    public static final String MESSAGE_AUTHENTICATION_FAIL = "MVP is unable to log in to your gmail account. Please "
            + "check the following:\n"
            + "1) You have entered the correct email address and password.\n"
            + "2) You have enabled 'Allow less secure app' in your gmail account settings.";
    public static final String MESSAGE_FAIL_UNKNOWN = "An unexpected error have occurred. Please try again later.";

    private final MessageDraft messageDraft;
    private final String[] emailLoginDetails;
    private final EmailFunction emailFunction;

    public EmailCommand(
            String message, String subject, String recipient,
            String [] emailLoginDetails, EmailFunction emailFunction) {
        this.messageDraft = new MessageDraft(message, subject, recipient);
        this.emailFunction = emailFunction;
        this.emailLoginDetails = emailLoginDetails;
    }

    /**
     * Identifies the Email Command Execution Task purpose
     *
     * @throws EmailLoginInvalidException if email login details is empty
     * @throws EmailMessageEmptyException if email message is empty
     * @throws EmailRecipientsEmptyException if recipients list is empty
     * @throws AuthenticationFailedException if gmail account can't be logged in
     */
    private void identifyEmailTask() throws EmailLoginInvalidException, EmailMessageEmptyException,
            EmailRecipientsEmptyException, AuthenticationFailedException {
        switch (emailFunction.getEmailFunction()) {
        case EmailFunction.EMAIL_FUNCTION_SEND:
            model.sendEmail(messageDraft);
            break;
        case EmailFunction.EMAIL_FUNCTION_CLEAR:
            model.clearEmailDraft();
            break;
        default:
            model.draftEmail(messageDraft);
            break;
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        requireNonNull(model);

        try {
            model.loginEmailAccount(emailLoginDetails);
            identifyEmailTask();
            return new CommandResult(String.format(MESSAGE_SUCCESS, model.getEmailStatus()));
        } catch (EmailLoginInvalidException e) {
            throw new CommandException(MESSAGE_LOGIN_INVALID);
        } catch (EmailMessageEmptyException e) {
            throw new CommandException(MESSAGE_EMPTY_INVALID);
        } catch (EmailRecipientsEmptyException e) {
            throw new CommandException(MESSAGE_RECIPIENT_INVALID);
        } catch (AuthenticationFailedException e) {
            throw new CommandException(MESSAGE_AUTHENTICATION_FAIL);
        } catch (RuntimeException e) {
            throw new CommandException(MESSAGE_FAIL_UNKNOWN);
        }
    }

    /**
     * Extracts Email from input recipient {@code recipient} into an InternetAddresss[] for sending email
     *
     * @param: recipient
     * @return: list of internet email address
     */
    public InternetAddress[] extractEmailFromContacts(String recipient) throws AddressException {
        InternetAddress[] recipientsEmail = new InternetAddress[1];
        try {
            recipientsEmail[0] = new InternetAddress(recipient);
        } catch (AddressException e) {
            throw new AddressException();
        }
        return recipientsEmail;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EmailCommand // instanceof handles nulls
                && ((EmailCommand) other).messageDraft.equals(this.messageDraft)
                && ((EmailCommand) other).loginDetailsEquals(this.emailLoginDetails)
                && ((EmailCommand) other).emailFunction.equals(this.emailFunction));
    }

    /**
     * Returns true for correct login details.
     */
    private boolean loginDetailsEquals(String [] other) {
        if (this.emailLoginDetails.length == other.length) {
            for (int i = 0; i < this.emailLoginDetails.length; i++) {
                if (this.emailLoginDetails[i] != other[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
