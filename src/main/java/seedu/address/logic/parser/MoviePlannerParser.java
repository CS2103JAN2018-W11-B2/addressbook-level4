package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddMovieCommand;
import seedu.address.logic.commands.AddScreeningCommand;
import seedu.address.logic.commands.AddTheaterCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteMovieCommand;
import seedu.address.logic.commands.DeleteTheaterCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditMovieCommand;
import seedu.address.logic.commands.EmailCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindMovieCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class MoviePlannerParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_ALIAS:
        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case AddMovieCommand.COMMAND_ALIAS:
        case AddMovieCommand.COMMAND_WORD:
            return new AddMovieCommandParser().parse(arguments);

        case AddScreeningCommand.COMMAND_ALIAS:
        case AddScreeningCommand.COMMAND_WORD:
            return new AddScreeningCommandParser().parse(arguments);

        case AddTheaterCommand.COMMAND_ALIAS:
        case AddTheaterCommand.COMMAND_WORD:
            return new AddTheaterCommandParser().parse(arguments);

        case EditCommand.COMMAND_ALIAS:
        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case EditMovieCommand.COMMAND_ALIAS:
        case EditMovieCommand.COMMAND_WORD:
            return new EditMovieCommandParser().parse(arguments);

        case EmailCommand.COMMAND_ALIAS:
        case EmailCommand.COMMAND_WORD:
            return new EmailCommandParser().parse(arguments);

        case SelectCommand.COMMAND_ALIAS:
        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_ALIAS:
        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case DeleteMovieCommand.COMMAND_ALIAS:
        case DeleteMovieCommand.COMMAND_WORD:
            return new DeleteMovieCommandParser().parse(arguments);

        case DeleteTheaterCommand.COMMAND_ALIAS:
        case DeleteTheaterCommand.COMMAND_WORD:
            return new DeleteTheaterCommandParser().parse(arguments);

        case ClearCommand.COMMAND_ALIAS:
        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_ALIAS:
        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case FindMovieCommand.COMMAND_ALIAS:
        case FindMovieCommand.COMMAND_WORD:
            return new FindMovieCommandParser().parse(arguments);

        case ListCommand.COMMAND_ALIAS:
        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case HistoryCommand.COMMAND_ALIAS:
        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_ALIAS:
        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_ALIAS:
        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_ALIAS:
        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
