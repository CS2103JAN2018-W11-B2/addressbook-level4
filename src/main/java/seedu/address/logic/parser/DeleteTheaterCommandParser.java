package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUMOFTHEATERS;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DeleteTheaterCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteTheaterCommand object
 */
public class DeleteTheaterCommandParser implements Parser<DeleteTheaterCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteTheaterCommand
     * and returns an DeleteTheaterCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteTheaterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NUMOFTHEATERS);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteTheaterCommand.MESSAGE_USAGE));
        }

        DeleteTheaterCommand.ResizeCinemaDescriptor resizeCinemaDescriptor =
                new DeleteTheaterCommand.ResizeCinemaDescriptor();
        try {
            ParserUtil.parseTheaters(argMultimap.getValue(PREFIX_NUMOFTHEATERS))
                    .ifPresent(resizeCinemaDescriptor::setTheaters);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!resizeCinemaDescriptor.isAnyFieldEdited()) {
            throw new ParseException(DeleteTheaterCommand.MESSAGE_NOT_RESIZED);
        }

        return new DeleteTheaterCommand(index, resizeCinemaDescriptor.getTheaterSize());
    }

}
