package seedu.todolist.commons.events.model;

import seedu.todolist.commons.events.BaseEvent;
import seedu.todolist.model.ReadOnlyTaskManager;

/** Indicates the TaskManager in the model has changed*/
public class TaskManagerChangedEvent extends BaseEvent {

    public final ReadOnlyTaskManager data;

    public TaskManagerChangedEvent(ReadOnlyTaskManager data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of entries " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}
