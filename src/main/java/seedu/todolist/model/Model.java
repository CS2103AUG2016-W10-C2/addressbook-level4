package seedu.todolist.model;

import seedu.todolist.commons.core.UnmodifiableObservableList;
import seedu.todolist.model.task.Entry;
import seedu.todolist.model.task.UniqueTaskList;
import seedu.todolist.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.todolist.model.task.UniqueTaskList.EntryConversionException;
import seedu.todolist.model.task.UniqueTaskList.EntryNotFoundException;
import seedu.todolist.model.tag.Tag;
import seedu.todolist.model.tag.UniqueTagList;
import seedu.todolist.model.task.Update;
import seedu.todolist.model.tag.UniqueTagList.DuplicateTagException;

import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskManager newData);

    /** Returns the TaskManager */
    ReadOnlyTaskManager getTaskManager();

    /** Deletes the given task. */
    void deleteTask(Entry target) throws EntryNotFoundException;

    /** Edit the given task 
     * @throws EntryConversionException */
    void editTask(Update update)
            throws EntryNotFoundException, DuplicateTaskException, EntryConversionException;

    /** Adds the given task */
    void addTask(Entry entry) throws UniqueTaskList.DuplicateTaskException;
    
    /** Adds the given tag 
     * @throws DuplicateTagException */
    void addTag(Tag tag) throws DuplicateTagException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<Entry> getFilteredPersonList();

    /** Updates the filter of the filtered task list to show all entries */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to show all entries excluding completed ones */
    void updateFilteredListToShowAllWithoutCompleted();

    /** Updates the filter of the filtered task list*/
    void updateFilteredEntryListPredicate(Predicate<Entry> predicate);
    
    /** Marks the given task */
    void markTask(Entry entryToMark) throws EntryNotFoundException;
    
    /** Unmarks the given task  */
    void unmarkTask(Entry task) throws EntryNotFoundException;

    /** Add tags to task
     */
    void tagTask(Entry taskToTag, UniqueTagList tagsToAdd) throws EntryNotFoundException;

    /** Remove tags from task
     */
    void untagTask(Entry taskToUntag, UniqueTagList tagsToRemove) throws EntryNotFoundException;

    /** Update the task's lastModifiedTime to the current time */
    void updateLastModifiedTime(Entry entry) throws EntryNotFoundException;
    
    /** Update the task's lastModifiedTime to the given date time */
    void updateLastModifiedTime(Entry entry, LocalDateTime localDateTime) throws EntryNotFoundException;

}
