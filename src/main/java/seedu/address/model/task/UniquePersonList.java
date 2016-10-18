package seedu.address.model.task;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.commons.exceptions.DuplicateDataException;

import java.util.Iterator;


/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see FloatingTask#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniquePersonList implements Iterable<Entry> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateTaskException extends DuplicateDataException {
        protected DuplicateTaskException() {
            super("Operation would result in duplicate persons");
        }
    }

    /**
     * Signals that an operation targeting a specified task in the list would fail because
     * there is no such matching task in the list.
     */
    public static class PersonNotFoundException extends Exception {}

    private final ObservableList<Entry> internalList = FXCollections.observableArrayList(
            new Callback<Entry, Observable[]>() {
        @Override
        public Observable[] call(Entry entry) {
            return new Observable[] { entry.titleObjectProperty(), entry.uniqueTagListObjectProperty(), entry.descriptionProperty()};
        }
    });

    /**
     * Constructs empty PersonList.
     */
    public UniquePersonList() {}

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(Entry toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException
     *             if the task to add is a duplicate of an existing task in the
     *             list.
     */
    public void add(Entry person) throws DuplicateTaskException {
        assert person != null;
        if (contains(person)) {
            throw new DuplicateTaskException();
        }
        internalList.add(person);
    }

    /**
     * Update the Title of the given Entry, if the given argument is not null
     * @param toEdit
     *          the Task to be edited
     * @param newTitle
     *          the new Title for the Task
     * @throws DuplicateTaskException
     *          if an existing Task already has the same Title as the one specified
     * @throws PersonNotFoundException
     *          if the Task to be edited cannot be found
     */
    public void updateTitle(Entry toEdit, Title newTitle) throws DuplicateTaskException, PersonNotFoundException {
        assert toEdit != null;
        for (int i = 0; i < internalList.size(); i++) {
            if (internalList.get(i).getTitle().equals(newTitle)) {
                throw new DuplicateTaskException();
            }
        }

        if (!contains(toEdit)) {
            throw new PersonNotFoundException();
        }

        if (newTitle != null) {
            toEdit.setTitle(newTitle);
        }
    }

    /**
     * Update the Tags of the given Entry, if the given argument is not null
     * @param toEdit
     *          the Task to be Edited
     * @param newTags
     *          the new Tags for the Task
     * @throws PersonNotFoundException
     *          if the Task to be edited cannot be found
     */
    public void updateTags(Entry toEdit, UniqueTagList newTags) throws PersonNotFoundException {
        assert toEdit != null;
        if (!contains(toEdit)) {
            throw new PersonNotFoundException();
        }

        if (newTags != null) {
            toEdit.setTags(newTags);
        }
    }

    /**
     * Update the Description of the given Entry, if the given argument is not null
     * @param toEdit
     *          the Task to be Edited
     * @param newDescription
     *          the new description for the Task
     * @throws PersonNotFoundException
     *          if the Task to be edited cannot be found
     */
    public void updateDescription(Entry toEdit, String newDescription) throws PersonNotFoundException {
        assert toEdit != null;
        if (!contains(toEdit)) {
            throw new PersonNotFoundException();
        }

        if (newDescription != null) {
            toEdit.setDescription(newDescription);
        }
    }
    
    /**
     * Mark an entry on the list.
     * @throws PersonNotFoundException 
     *             if no such task could be found in the list.
     * @throws DuplicateTaskException 
     *             if the task to add is a duplicate of an existing task.
     */
    public void mark(Entry toMark) throws PersonNotFoundException, DuplicateTaskException {
        assert toMark!= null;
        remove(toMark);
        toMark.mark();
        add(toMark);
    }

    /**
     * Unmarks an entry on the list.
     * @throws PersonNotFoundException 
     *             if no such task could be found in the list.
     * @throws DuplicateTaskException 
     *             if the task to add is a duplicate of an existing task.
     */
    public void unmark(Entry toUnmark) throws PersonNotFoundException, DuplicateTaskException {
        assert toUnmark!= null;
        remove(toUnmark);
        toUnmark.unmark();
        add(toUnmark);
    }
    
    /**
     * Removes the equivalent task from the list.
     *
     * @throws PersonNotFoundException if no such task could be found in the list.
     */
    public boolean remove(Entry toRemove) throws PersonNotFoundException {
        assert toRemove != null;
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new PersonNotFoundException();
        }
        return personFoundAndDeleted;
    }

    public ObservableList<Entry> getInternalList() {
        return internalList;
    }

    @Override
    public Iterator<Entry> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                && this.internalList.equals(
                ((UniquePersonList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

}
