package seedu.address.logic.commands;

/**
 * Lists all movies in the address book to the user.
 */
public class ListMovieCommand extends Command {

    public static final String COMMAND_WORD = "listmovies";
    public static final String COMMAND_ALIAS = "lm";
    public static final String MESSAGE_SUCCESS = "Listed all movies";


    @Override
    public CommandResult execute() {
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
