package seedu.priorityq.logic.commands;

import seedu.priorityq.commons.core.EventsCenter;
import seedu.priorityq.commons.core.Messages;
import seedu.priorityq.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.priorityq.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of entries.
     *
     * @param displaySize used to generate summary
     * @return summary message for entries displayed
     */
    public static String getMessageForEntryListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_ENTRY_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute();

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setData(Model model) {
        this.model = model;
    }

    /**
     * Raises an event to indicate an attempt to execute an incorrect command
     */
    protected void indicateAttemptToExecuteIncorrectCommand() {
        EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent(this));
    }
}
