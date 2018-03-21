package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUMOFTHEATERS;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddTheaterCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddTheaterCommand object
 */
public class AddTheaterCommandParser implements Parser<AddTheaterCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddTheaterCommand
     * and returns an AddTheaterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddTheaterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NUMOFTHEATERS);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddTheaterCommand.MESSAGE_USAGE));
        }

        AddTheaterCommand.ResizeCinemaDescriptor resizeCinemaDescriptor =
                new AddTheaterCommand.ResizeCinemaDescriptor();
        try {
            ParserUtil.parseTheaters(argMultimap.getValue(PREFIX_NUMOFTHEATERS))
                    .ifPresent(resizeCinemaDescriptor::setTheaters);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!resizeCinemaDescriptor.isAnyFieldEdited()) {
            throw new ParseException(AddTheaterCommand.MESSAGE_NOT_RESIZED);
        }

        return new AddTheaterCommand(index, resizeCinemaDescriptor.getTheaterSize());
    }

}
