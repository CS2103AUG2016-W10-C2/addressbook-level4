package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.logic.commands.MarkCommand;

/**
 * An event indicating that the user has clicked on a checkbox for a particular
 * task in the GUI.
 */
public class MarkTaskEvent extends BaseEvent {

    private final static String COMPLETED = "completed";
    private final static String UN_COMPLETE = "unmarked";

    // The index of the task in the current view which was acted upon.
    private int targetTaskIndex;

    // The new completion indicator, which is true if the task was just completed
    private boolean shouldMark;

    public MarkTaskEvent(int idx, boolean newValue) {
        targetTaskIndex = idx;
        shouldMark = newValue;
    }

    public String getCommandString() {
        return MarkCommand.COMMAND_WORD + " " + targetTaskIndex;
    }

    @Override
    public String toString() {
        String action = shouldMark ? COMPLETED : UN_COMPLETE;
        return String.format("User %s task at index %d", action, targetTaskIndex);
    }

}
