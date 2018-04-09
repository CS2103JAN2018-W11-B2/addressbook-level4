package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RATING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.EditMovieCommand;
import seedu.address.logic.commands.EditMovieCommand.EditMovieDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
//@@author slothhy-unused
/**
 * This is not used because editing the duration might mess up the screening timings
 * for those that are already added to the theaters.
 * Parses input arguments and creates a new EditMovieCommand object
 */
public class EditMovieCommandParser implements Parser<EditMovieCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditMovieCommand
     * and returns an EditMovieCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditMovieCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DURATION, PREFIX_RATING,
                        PREFIX_STARTDATE, PREFIX_TAG);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditMovieCommand.MESSAGE_USAGE));
        }

        EditMovieDescriptor editMovieDescriptor = new EditMovieDescriptor();
        try {
            ParserUtil.parseMovieName(argMultimap.getValue(PREFIX_NAME)).ifPresent(editMovieDescriptor::setName);
            ParserUtil.parseDuration(argMultimap.getValue(PREFIX_DURATION)).ifPresent(editMovieDescriptor::setDuration);
            ParserUtil.parseRating(argMultimap.getValue(PREFIX_RATING)).ifPresent(editMovieDescriptor::setRating);
            ParserUtil.parseStartDate(argMultimap.getValue(PREFIX_STARTDATE))
                    .ifPresent(editMovieDescriptor::setStartDate);
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editMovieDescriptor::setTags);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editMovieDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditMovieCommand.MESSAGE_NOT_EDITED);
        }

        return new EditMovieCommand(index, editMovieDescriptor);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws IllegalValueException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
