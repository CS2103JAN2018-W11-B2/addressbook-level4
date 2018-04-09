package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTDATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.function.Predicate;

import seedu.address.model.movie.Movie;
import seedu.address.model.movie.NameAndStartDateAndTagContainsKeywordsPredicate;
import seedu.address.model.movie.NameAndStartDateContainsKeywordsPredicate;
import seedu.address.model.movie.NameAndTagContainsKeywordsPredicate;
import seedu.address.model.movie.NameContainsKeywordsPredicate;
import seedu.address.model.movie.StartDateAndTagContainsKeywordsPredicate;
import seedu.address.model.movie.StartDateContainsKeywordsPredicate;
import seedu.address.model.movie.TagContainsKeywordsPredicate;
//@@author slothhy
/**
 * Finds and lists all movies in movie planner whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindMovieCommand extends Command {

    public static final String COMMAND_WORD = "findmovie";
    public static final String COMMAND_ALIAS = "fm";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all movies whose names contain any of "
            + "parameters specified and their specified keywords (case-insensitive).\n"
            + "Parameters: "
            + "[" + PREFIX_NAME + "KEYWORDS] "
            + "[" + PREFIX_STARTDATE + "STARTDATE] "
            + "[" + PREFIX_TAG + "TAG] "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "avenger horror nemo "
            + PREFIX_STARTDATE + "20/10/2015 "
            + PREFIX_TAG + "superhero";

    private final Predicate<Movie> predicate;

    public  FindMovieCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(NameAndStartDateContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(NameAndStartDateAndTagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(NameAndTagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public  FindMovieCommand(StartDateContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(StartDateAndTagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public FindMovieCommand(TagContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }


    @Override
    public CommandResult execute() {
        model.updateFilteredMovieList(predicate);
        return new CommandResult(getMessageForMovieListShownSummary(model.getFilteredMovieList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindMovieCommand // instanceof handles nulls
                && this.predicate.equals(((FindMovieCommand) other).predicate)); // state check
    }

}
