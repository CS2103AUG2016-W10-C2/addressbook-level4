package seedu.address.model;

import javafx.collections.transformation.FilteredList;
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

import java.time.LocalDateTime;
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
        filteredEntries = new FilteredList<>(taskManager.getSortedEntries());
        updateFilteredListToShowAllWithoutCompleted();
    }

    public ModelManager() {
        this(new TaskManager(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskManager initialData, UserPrefs userPrefs) {
        taskManager = new TaskManager(initialData);
        filteredEntries = new FilteredList<>(taskManager.getSortedEntries());
        updateFilteredListToShowAllWithoutCompleted();
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
        updateFilteredListToShowAllWithoutCompleted();
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void editTask(Update update)
            throws EntryNotFoundException, DuplicateTaskException, EntryConversionException {
        taskManager.editTask(update);
        updateFilteredListToShowAllWithoutCompleted();
        indicateAddressBookChanged();
    }

    @Override
    public void markTask(Entry task) throws EntryNotFoundException {
        taskManager.markTask(task);
        updateFilteredListToShowAllWithoutCompleted();
        indicateAddressBookChanged();
    }

    @Override
    public void unmarkTask(Entry task) throws EntryNotFoundException {
        taskManager.unmarkTask(task);
        updateFilteredListToShowAllWithoutCompleted();
        indicateAddressBookChanged();
    }

    @Override
    public void tagTask(Entry taskToTag, UniqueTagList tagsToAdd) {
        taskManager.tagTask(taskToTag, tagsToAdd);
        updateFilteredListToShowAllWithoutCompleted();
        indicateAddressBookChanged();
    }

    @Override
    public void untagTask(Entry taskToUntag, UniqueTagList tagsToRemove) throws EntryNotFoundException {
        taskManager.untagTask(taskToUntag, tagsToRemove);
        updateFilteredListToShowAllWithoutCompleted();
        indicateAddressBookChanged();
    }

    @Override
    public void addTag(Tag tag) throws DuplicateTagException {
        taskManager.addTag(tag);
    }

    @Override
    public void updateLastModifiedTime(Entry entry) throws EntryNotFoundException {
        taskManager.updateLastModifiedTime(entry);
    }

    @Override
    public void updateLastModifiedTime(Entry entry, LocalDateTime localDateTime) throws EntryNotFoundException {
        taskManager.updateLastModifiedTime(entry, localDateTime);
    }
    //=========== Filtered Person List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<Entry> getFilteredPersonList() {
        return new UnmodifiableObservableList<>(filteredEntries);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredEntries.setPredicate(null);
    }

    @Override
    public void updateFilteredListToShowAllWithoutCompleted() {
        Predicate<Entry> predicate = e -> !e.isMarked();
        filteredEntries.setPredicate(predicate);
    }

    @Override
    public void updateFilteredEntryListPredicate(Predicate<Entry> predicate) {
        updateFilteredPersonList(predicate);
    }

    private void updateFilteredPersonList(Predicate<Entry> predicate) {
        filteredEntries.setPredicate(predicate);
    }
}
