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
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Entry> {

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
    public static class EntryNotFoundException extends Exception {}

    private final ObservableList<Entry> internalList = FXCollections.observableArrayList(
            new Callback<Entry, Observable[]>() {
        @Override
        public Observable[] call(Entry entry) {
        	if (entry instanceof Task) {
        		return new Observable[] { entry.titleObjectProperty(), ((Task)entry).deadlineObjectProperty(), entry.uniqueTagListObjectProperty(), entry.descriptionProperty(), entry.isMarkedProperty()};
        	} else if (entry instanceof Event){
        		return new Observable[] { entry.titleObjectProperty(), ((Event)entry).startTimeObjectProperty(), ((Event)entry).endTimeObjectProperty(), entry.uniqueTagListObjectProperty(), entry.descriptionProperty(), entry.isMarkedProperty()};
        	} else {
        		return new Observable[] { entry.titleObjectProperty(), entry.uniqueTagListObjectProperty(), entry.descriptionProperty(), entry.isMarkedProperty()};
        	}
        }
    });

    /**
     * Constructs empty PersonList.
     */
    public UniqueTaskList() {}

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
    public void add(Entry entry) throws DuplicateTaskException {
        assert entry != null;
        if (contains(entry)) {
            throw new DuplicateTaskException();
        }
        internalList.add(entry);
    }

    /**
     * Update the Title of the given Entry, if the given argument is not null
     * @param toEdit
     *          the Task to be edited
     * @param newTitle
     *          the new Title for the Task
     * @throws DuplicateTaskException
     *          if an existing Task already has the same Title as the one specified
     * @throws EntryNotFoundException
     *          if the Task to be edited cannot be found
     */
    public void updateTitle(Entry toEdit, Title newTitle) throws DuplicateTaskException, EntryNotFoundException {
        assert toEdit != null;
        for (int i = 0; i < internalList.size(); i++) {
            if (internalList.get(i).getTitle().equals(newTitle)) {
                throw new DuplicateTaskException();
            }
        }

        if (!contains(toEdit)) {
            throw new EntryNotFoundException();
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
     * @throws EntryNotFoundException
     *          if the Task to be edited cannot be found
     */
    public void updateTags(Entry toEdit, UniqueTagList newTags) throws EntryNotFoundException {
        assert toEdit != null;
        if (!contains(toEdit)) {
            throw new EntryNotFoundException();
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
     * @throws EntryNotFoundException
     *          if the Task to be edited cannot be found
     */
    public void updateDescription(Entry toEdit, String newDescription) throws EntryNotFoundException {
        assert toEdit != null;
        if (!contains(toEdit)) {
            throw new EntryNotFoundException();
        }

        if (newDescription != null) {
            toEdit.setDescription(newDescription);
        }
    }

    /**
     * Mark an entry on the list.
     * @throws EntryNotFoundException
     *             if no such task could be found in the list.
     * @throws DuplicateTaskException
     *             if the task to add is a duplicate of an existing task.
     */
    public void mark(Entry toMark) throws EntryNotFoundException, DuplicateTaskException {
        assert toMark!= null;
        if (!contains(toMark)) {
            throw new EntryNotFoundException();
        }
        toMark.mark();
    }

    /**
     * Unmarks an entry on the list.
     * @throws EntryNotFoundException
     *             if no such task could be found in the list.
     * @throws DuplicateTaskException
     *             if the task to add is a duplicate of an existing task.
     */
    public void unmark(Entry toUnmark) throws EntryNotFoundException, DuplicateTaskException {
        assert toUnmark!= null;
        if (!contains(toUnmark)) {
            throw new EntryNotFoundException();
        }
        toUnmark.unmark();
    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws EntryNotFoundException if no such task could be found in the list.
     */
    public boolean remove(Entry toRemove) throws EntryNotFoundException {
        assert toRemove != null;
        final boolean personFoundAndDeleted = internalList.remove(toRemove);
        if (!personFoundAndDeleted) {
            throw new EntryNotFoundException();
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
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

}
