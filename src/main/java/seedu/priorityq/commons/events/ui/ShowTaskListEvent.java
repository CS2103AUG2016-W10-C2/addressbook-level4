package seedu.priorityq.commons.events.ui;

import seedu.priorityq.commons.events.BaseEvent;

/**
 * An event requesting to view the task list.
 */
public class ShowTaskListEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
