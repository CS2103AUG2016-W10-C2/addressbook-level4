package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniquePersonList;
import seedu.address.model.task.UniquePersonList.DuplicateTaskException;
import seedu.address.model.task.UniquePersonList.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Update;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Predicate;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /** Deletes the given task. */
    void deleteTask(Entry target) throws UniquePersonList.PersonNotFoundException;

    /** Edit the given task */
    void editTask(Update update)
            throws PersonNotFoundException, DuplicateTaskException;

    /** Adds the given task */
    void addTask(Entry entry) throws UniquePersonList.DuplicateTaskException;
    
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
    void markTask(Entry entryToMark) throws UniquePersonList.PersonNotFoundException, DuplicateTaskException;
    
    /** Unmarks the given task 
     * @throws  */
    void unmarkTask(Entry task) throws UniquePersonList.PersonNotFoundException, DuplicateTaskException;

    /** Add tags to task
     */
    void tagTask(Entry taskToTag, UniqueTagList tagsToAdd) throws PersonNotFoundException;

    /** Remove tags from task
     */
    void untagTask(Entry taskToUntag, UniqueTagList tagsToRemove) throws PersonNotFoundException;

}
