package seedu.address.model.task;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.DuplicateDataException;

import java.time.LocalDateTime;
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
            super("Operation would result in duplicate entries");
        }
    }

    /**
     * Signals that an operation targeting a specified task in the list would fail because
     * there is no such matching task in the list.
     */
    public static class EntryNotFoundException extends Exception {}

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class EntryConversionException extends DataConversionException {
        public EntryConversionException(Exception cause) {
            super(cause);
        }

        public EntryConversionException() {
            super(new Exception());
        }
    }

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
        Entry copy;
        if (toEdit instanceof Task) {
            copy = new Task(toEdit);
        } else {
            copy = new Event(toEdit);
        }
        copy.setTitle(newTitle);

        if (contains(copy)) {
            throw new DuplicateTaskException();
        }

        if (!contains(toEdit)) {
            throw new EntryNotFoundException();
        }

        if (newTitle != null) {
            toEdit.setTitle(newTitle);
        }
    }

    /**
     * Update the Start Date of the given Entry, if the given argument is not null and the Entry is an Event
     * @param toEdit
     *          the Event to be edited
     * @param newStartTime
     *          the new Start Time for the Event
     * @throws EntryNotFoundException
     *          if the Event to be edited cannot be found
     * @throws EntryConversionException
     *          if the Entry to be edited is not an Event
     */
    public void updateStartTime(Entry toEdit, LocalDateTime newStartTime) throws EntryNotFoundException, EntryConversionException {
        assert toEdit != null;

        if (!contains(toEdit)) {
            throw new EntryNotFoundException();
        }

        if (newStartTime != null) {
            if (toEdit instanceof Event) {
                ((Event) toEdit).setStartTime(newStartTime);
            }
            else {
                throw new EntryConversionException();
            }
        }
    }

    /**
     * Update the endTime of Entry. The type of the Entry won't change.
     * @param toEdit
     *          the Entry to be Edited
     * @param newEndTime
     *          the new EndTime for the Entry
     * @throws EntryNotFoundException
     *          if the Entry to be edited cannot be found
     */
    public void updateEndTime(Entry toEdit, LocalDateTime newEndTime) throws EntryNotFoundException {
        assert toEdit != null;
        if (!contains(toEdit)) {
            throw new EntryNotFoundException();
        }

        if (newEndTime != null) {
            if (toEdit instanceof Task) {
                ((Task)toEdit).setDeadline(newEndTime);
            }

            if (toEdit instanceof Event) {
                ((Event)toEdit).setEndTime(newEndTime);
            }
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
     */
    public void mark(Entry toMark) throws EntryNotFoundException {
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
     */
    public void unmark(Entry toUnmark) throws EntryNotFoundException {
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
