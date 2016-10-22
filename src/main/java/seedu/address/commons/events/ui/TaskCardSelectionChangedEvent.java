package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.task.Entry;

/**
 * Represents a selection change in the Person List Panel
 */
public class TaskCardSelectionChangedEvent extends BaseEvent {


    private final Entry newSelection;

    public TaskCardSelectionChangedEvent(Entry newSelection){
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public Entry getNewSelection() {
        return newSelection;
    }
}
