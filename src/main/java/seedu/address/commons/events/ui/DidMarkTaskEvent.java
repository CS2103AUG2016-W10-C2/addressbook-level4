package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.commands.CommandResult;

//@@author A0116603R
/**
 * An event indicating that the Logic component has completed
 * executing the mark/unmark command after a user clicks on
 * a checkbox in the GUI. This event is used to propagate feedback
 * to the user.
 */
public class DidMarkTaskEvent extends BaseEvent{

    private CommandResult cmdResult;

    public DidMarkTaskEvent(CommandResult cmdResult) {
        this.cmdResult = cmdResult;
    }

    public CommandResult getCommandResult() {
        return cmdResult;
    }

    @Override
    public String toString() {
        return cmdResult.feedbackToUser;
    }
}
