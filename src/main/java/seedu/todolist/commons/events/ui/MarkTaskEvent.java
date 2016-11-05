package seedu.todolist.commons.events.ui;

import seedu.todolist.commons.events.BaseEvent;
import seedu.todolist.logic.commands.MarkCommand;
import seedu.todolist.logic.commands.UnmarkCommand;

//@@author A0116603R
/**
 * An event indicating that the user has clicked on a checkbox for a particular
 * task in the GUI.
 */
public class MarkTaskEvent extends BaseEvent {

    private final static String CHECKED = "checked";
    private final static String UNCHECKED = "unchecked";

    // The index of the task in the current view which was acted upon.
    private int targetTaskIndex;

    // The new completion indicator, which is true if the task was just completed
    private boolean shouldMark;

    public MarkTaskEvent(int idx, boolean newValue) {
        targetTaskIndex = idx;
        shouldMark = newValue;
    }

    public String getCommandString() {
        String command = shouldMark ? MarkCommand.COMMAND_WORD : UnmarkCommand.COMMAND_WORD;
        return command + " " + targetTaskIndex;
    }

    @Override
    public String toString() {
        String action = shouldMark ? CHECKED : UNCHECKED;
        return String.format("User %s task at index %d", action, targetTaskIndex);
    }

}
