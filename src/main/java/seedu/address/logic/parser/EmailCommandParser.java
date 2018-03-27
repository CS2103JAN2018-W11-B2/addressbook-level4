package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_FUNCTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_LOGIN;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_MESSAGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_RECIPIENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL_SUBJECT;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.email.EmailFunction;
import seedu.address.logic.commands.EmailCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EmailCommand object
 */
public class EmailCommandParser implements Parser<EmailCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EmailCommand
     * and returns an EmailCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EmailCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EMAIL_MESSAGE, PREFIX_EMAIL_SUBJECT, PREFIX_EMAIL_LOGIN,
                        PREFIX_EMAIL_FUNCTION, PREFIX_EMAIL_RECIPIENT);

        EmailFunction emailFunction = new EmailFunction();
        String message;
        String subject;
        String recipient;
        String[] loginDetails;

        try {
            message = getArgumentMessage(argMultimap);
            subject = getArgumentSubject(argMultimap);
            recipient = getArgumentRecipient(argMultimap);
            loginDetails = getArgumentLoginDetails(argMultimap);
            emailFunction = getArgumentEmailFunction(argMultimap, emailFunction);

            if (message.isEmpty() && subject.isEmpty() && recipient.isEmpty() &&  (
                    loginDetails.length == 0) && !emailFunction.isValid()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
            }

        } catch (IllegalValueException e) {
            throw new ParseException(e.getMessage(), e);
        }

        return new EmailCommand(message, subject, recipient, loginDetails, emailFunction);
    }

    /**
     * Returns argument message values if available
     *
     * @param argMultimap
     * @return argument message values
     * @throws IllegalValueException if value is empty
     */
    private String getArgumentMessage(ArgumentMultimap argMultimap) throws IllegalValueException {
        String message = "";

        if (argMultimap.getValue(PREFIX_EMAIL_MESSAGE).isPresent()) {
            message = ParserUtil.parseEmailMessage(argMultimap.getValue(PREFIX_EMAIL_MESSAGE)).trim();
            if (message.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
            }
        }
        return message;
    }

    /**
     * Returns argument subject values if available
     *
     * @param argMultimap
     * @return argument subject values
     * @throws IllegalValueException if value is empty
     */
    private String getArgumentSubject(ArgumentMultimap argMultimap) throws IllegalValueException {
        String subject = "";

        if (argMultimap.getValue(PREFIX_EMAIL_SUBJECT).isPresent()) {
            subject = ParserUtil.parseEmailMessage(argMultimap.getValue(PREFIX_EMAIL_SUBJECT)).trim();
            if (subject.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
            }
        }
        return subject;
    }

    /**
     * Returns argument login values if available
     *
     * @param argMultimap
     * @return argument login values
     * @throws IllegalValueException if value is empty
     */
    private String [] getArgumentLoginDetails(ArgumentMultimap argMultimap) throws IllegalValueException {
        String login = "";
        String [] loginDetails = new String[0];

        if (argMultimap.getValue(PREFIX_EMAIL_LOGIN).isPresent()) {
            login = ParserUtil.parseEmailLoginDetails(argMultimap.getValue(PREFIX_EMAIL_LOGIN)).trim();
            loginDetails = login.split(":");
            if (loginDetails.length != 2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
            }
        }
        return loginDetails;
    }

    /**
     * Returns argument emailFunction values if available
     *
     * @param argMultimap
     * @param emailFunction new EmailFunction object
     * @return emailFunction with argument emailFunction values
     * @throws IllegalValueException if emailFunction is not "send" or "clear"
     */
    private EmailFunction getArgumentEmailFunction(
            ArgumentMultimap argMultimap, EmailFunction emailFunction) throws IllegalValueException {
        if (argMultimap.getValue(PREFIX_EMAIL_FUNCTION).isPresent()) {
            emailFunction.setEmailFunction(
                    ParserUtil.parseEmailTask(argMultimap.getValue(PREFIX_EMAIL_FUNCTION)).trim());
            if (!emailFunction.isValid()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
            }
        }
        return emailFunction;
    }

    /**
     * Returns argument recipient values if available
     *
     * @param argMultimap
     * @return argument recipient values
     * @throws IllegalValueException if value is empty
     */
    private String getArgumentRecipient(ArgumentMultimap argMultimap) throws IllegalValueException {
        String recipient = "";

        if (argMultimap.getValue(PREFIX_EMAIL_RECIPIENT).isPresent()) {
            recipient = ParserUtil.parseEmailMessage(argMultimap.getValue(PREFIX_EMAIL_RECIPIENT)).trim();
            if (recipient.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EmailCommand.MESSAGE_USAGE));
            }
        }
        return recipient;
    }
}
