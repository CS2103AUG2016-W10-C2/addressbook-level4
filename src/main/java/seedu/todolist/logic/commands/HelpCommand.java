package seedu.todolist.logic.commands;


import seedu.todolist.commons.core.EventsCenter;
import seedu.todolist.commons.events.ui.ShowHelpListEvent;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "";

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ShowHelpListEvent());
        return new CommandResult(SHOWING_HELP_MESSAGE);
    }
}
