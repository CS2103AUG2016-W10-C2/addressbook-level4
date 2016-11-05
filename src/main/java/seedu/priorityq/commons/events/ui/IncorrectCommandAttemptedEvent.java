package seedu.priorityq.commons.events.ui;

import seedu.priorityq.commons.events.BaseEvent;
import seedu.priorityq.logic.commands.Command;

/**
 * Indicates an attempt to execute an incorrect command
 */
public class IncorrectCommandAttemptedEvent extends BaseEvent {

    public IncorrectCommandAttemptedEvent(Command command) {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
