package seedu.address.model;

import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.task.Title;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniquePersonList;
import seedu.address.model.task.UniquePersonList.DuplicateTaskException;
import seedu.address.model.task.UniquePersonList.PersonNotFoundException;
import seedu.address.model.tag.UniqueTagList;

import java.util.Set;

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
    void editTask(Entry task, Title title, UniqueTagList newTags)
            throws PersonNotFoundException, DuplicateTaskException;

    /** Adds the given task */
    void addTask(Entry entry) throws UniquePersonList.DuplicateTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<Entry> getFilteredPersonList();

    /** Updates the filter of the filtered task list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredPersonList(Set<String> keywords);

    /** Marks the given task. 
     * @throws DuplicateTaskException */
    void markTask(Entry entryToMark) throws UniquePersonList.PersonNotFoundException, DuplicateTaskException;
    
    /** Unmarks the given task 
     * @throws  */
    void unmarkTask(Entry task) throws UniquePersonList.PersonNotFoundException, DuplicateTaskException;

}
