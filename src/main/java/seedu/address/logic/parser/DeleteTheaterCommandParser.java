package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUMOFTHEATERS;

import java.util.ArrayList;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DeleteTheaterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.cinema.Theater;

/**
 * Parses input arguments and creates a new DeleteTheaterCommand object
 */
public class DeleteTheaterCommandParser implements Parser<DeleteTheaterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteTheaterCommand
     * and returns a {@code DeleteTheaterCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteTheaterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NUMOFTHEATERS);

        Index index;
        ArrayList<Theater> newTheaters;

        if (!arePrefixesPresent(argMultimap, PREFIX_NUMOFTHEATERS)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTheaterCommand.MESSAGE_USAGE));
        }

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTheaterCommand.MESSAGE_USAGE));
        }

        try {
            newTheaters = ParserUtil.parseTheaters(argMultimap.getValue(PREFIX_NUMOFTHEATERS)).get();
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        return new DeleteTheaterCommand(index, newTheaters.size());
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
