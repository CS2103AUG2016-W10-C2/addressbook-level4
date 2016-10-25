package seedu.address.model;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.TaskManagerChangedEvent;
import seedu.address.commons.core.ComponentManager;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.EntryConversionException;
import seedu.address.model.task.UniqueTaskList.EntryNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Update;
import seedu.address.model.tag.UniqueTagList.DuplicateTagException;

import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskManager taskManager;
    private final FilteredList<Entry> filteredEntries;
    private final SortedList<Entry> sortedEntries;

    /**
     * Initializes a ModelManager with the given TaskManager
     * TaskManager and its variables should not be null
     */
    public ModelManager(TaskManager src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);

        taskManager = new TaskManager(src);
        filteredEntries = new FilteredList<>(taskManager.getEntries());
        sortedEntries = filteredEntries.sorted();
    }

    public ModelManager() {
        this(new TaskManager(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskManager initialData, UserPrefs userPrefs) {
        taskManager = new TaskManager(initialData);
        filteredEntries = new FilteredList<>(taskManager.getEntries());
        sortedEntries = filteredEntries.sorted();
    }

    @Override
    public void resetData(ReadOnlyTaskManager newData) {
        taskManager.resetData(newData);
        indicateAddressBookChanged();
    }

    public ReadOnlyTaskManager getTaskManager() {
        return taskManager;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new TaskManagerChangedEvent(taskManager));
    }

    @Override
    public synchronized void deleteTask(Entry target) throws EntryNotFoundException {
        taskManager.removeEntry(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addTask(Entry entry) throws UniqueTaskList.DuplicateTaskException {
        taskManager.addTask(entry);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void editTask(Update update)
            throws EntryNotFoundException, DuplicateTaskException, EntryConversionException {
        taskManager.editTask(update);
        indicateAddressBookChanged();
    }

    @Override
    public void markTask(Entry task) throws EntryNotFoundException {
        taskManager.markTask(task);
        indicateAddressBookChanged();
    }

    @Override
    public void unmarkTask(Entry task) throws EntryNotFoundException {
        taskManager.unmarkTask(task);
        indicateAddressBookChanged();
    }

    @Override
    public void tagTask(Entry taskToTag, UniqueTagList tagsToAdd) {
        taskManager.tagTask(taskToTag, tagsToAdd);
        indicateAddressBookChanged();
    }

    @Override
    public void untagTask(Entry taskToUntag, UniqueTagList tagsToRemove) throws EntryNotFoundException {
        taskManager.untagTask(taskToUntag, tagsToRemove);
        indicateAddressBookChanged();
    }

    @Override
    public void addTag(Tag tag) throws DuplicateTagException {
        taskManager.addTag(tag);
    }
    //=========== Filtered Person List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<Entry> getFilteredPersonList() {
        return new UnmodifiableObservableList<>(sortedEntries);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredEntries.setPredicate(null);
    }

    @Override
    public void updateFilteredEntryListPredicate(Predicate<Entry> predicate) {
        updateFilteredPersonList(predicate);
    }

    private void updateFilteredPersonList(Predicate<Entry> predicate) {
        filteredEntries.setPredicate(predicate);
    }
}
