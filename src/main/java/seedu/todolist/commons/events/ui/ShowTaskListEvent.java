package seedu.todolist.commons.events.ui;

import seedu.todolist.commons.events.BaseEvent;

/**
 * An event requesting to view the task list.
 */
public class ShowTaskListEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
