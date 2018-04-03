package seedu.address.logic.commands;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.JumpToDateRequestEvent;
import seedu.address.logic.parser.JumpCommandParser;

/**
 * Jumps to a specified date as specified by the user
 */
public class JumpCommand extends Command {

    public static final String COMMAND_WORD = "jump";
    public static final String COMMAND_ALIAS = "j";
    private static final String MESSAGE_JUMP = "Jumping to ";
    private static final String MESSAGE_WARNING = ", please select a cinema "
            + "if you do not see any schedule on the right.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Jumps to a date for the cinema's schedule. Please remember to input a valid date.\n"
            + "Parameters: dd/MM/yyyy\n"
            + "Example: " + COMMAND_WORD + " 05/01/2018";

    private LocalDate date;

    public JumpCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public CommandResult execute() {
        String dateToJump = date.format(DateTimeFormatter.ofPattern(JumpCommandParser.DATE_FORMAT));
        EventsCenter.getInstance().post(new JumpToDateRequestEvent(date));
        return new CommandResult(MESSAGE_JUMP + dateToJump + MESSAGE_WARNING);
    }
}
