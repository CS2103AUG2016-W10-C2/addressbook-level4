package seedu.priorityq.model;


import seedu.priorityq.model.task.Entry;
import seedu.priorityq.model.task.UniqueTaskList;
import seedu.priorityq.model.tag.Tag;
import seedu.priorityq.model.tag.UniqueTagList;

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
