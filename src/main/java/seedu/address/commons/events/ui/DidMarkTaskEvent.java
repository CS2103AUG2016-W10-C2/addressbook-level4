package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.commands.CommandResult;

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
