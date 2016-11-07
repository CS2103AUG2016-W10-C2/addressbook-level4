package seedu.priorityq.model;

import seedu.priorityq.commons.core.UnmodifiableObservableList;
import seedu.priorityq.model.entry.Entry;
import seedu.priorityq.model.entry.UniqueTaskList;
import seedu.priorityq.model.entry.Update;
import seedu.priorityq.model.entry.UniqueTaskList.DuplicateTaskException;
import seedu.priorityq.model.entry.UniqueTaskList.EntryConversionException;
import seedu.priorityq.model.entry.UniqueTaskList.EntryNotFoundException;
import seedu.priorityq.model.tag.Tag;
import seedu.priorityq.model.tag.UniqueTagList;
import seedu.priorityq.model.tag.UniqueTagList.DuplicateTagException;

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
            throws EntryNotFoundException, EntryConversionException;

    /** Adds the given task 
     * @throws DuplicateTaskException */
    void addTask(Entry entry) throws DuplicateTaskException;
    
    /** Adds the given tag 
     * @throws DuplicateTagException */
    void addTag(Tag tag) throws DuplicateTagException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<Entry> getFilteredEntryList();

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
