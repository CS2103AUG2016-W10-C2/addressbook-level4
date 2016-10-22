package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Update;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;

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
    void deleteTask(Entry target) throws UniqueTaskList.PersonNotFoundException;

    /** Edit the given task */
    void editTask(Update update)
            throws PersonNotFoundException, DuplicateTaskException;

    /** Adds the given task */
    void addTask(Entry entry) throws UniqueTaskList.DuplicateTaskException;
    
    /** Adds the given tag 
     * @throws DuplicateTagException */
    void addTag(Tag tag) throws DuplicateTagException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<Entry> getFilteredPersonList();

    /** Updates the filter of the filtered task list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list*/
    void updateFilteredEntryListPredicate(Predicate<Entry> predicate);
    
    /** Marks the given task. 
     * @throws DuplicateTaskException */
    void markTask(Entry entryToMark) throws UniqueTaskList.PersonNotFoundException, DuplicateTaskException;
    
    /** Unmarks the given task 
     * @throws  */
    void unmarkTask(Entry task) throws UniqueTaskList.PersonNotFoundException, DuplicateTaskException;

    /** Add tags to task
     */
    void tagTask(Entry taskToTag, UniqueTagList tagsToAdd) throws PersonNotFoundException;

    /** Remove tags from task
     */
    void untagTask(Entry taskToUntag, UniqueTagList tagsToRemove) throws PersonNotFoundException;

}
