package seedu.todolist.model;


import seedu.todolist.model.task.Entry;
import seedu.todolist.model.task.UniqueTaskList;
import seedu.todolist.model.tag.Tag;
import seedu.todolist.model.tag.UniqueTagList;

import java.util.List;

/**
 * Unmodifiable view of a task manager
 */
public interface ReadOnlyTaskManager {

    UniqueTagList getUniqueTagList();

    UniqueTaskList getUniqueTaskList();

    /**
     * Returns an unmodifiable view of tasks list
     */
    List<Entry> getTaskList();

    /**
     * Returns an unsorted unmodifiable view of tasks list
     */
    List<Entry> getUnsortedTaskList();

    /**
     * Returns an unmodifiable view of tags list
     */
    List<Tag> getTagList();

}
