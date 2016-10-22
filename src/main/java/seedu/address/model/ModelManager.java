package seedu.address.model;

import javafx.collections.transformation.FilteredList;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.core.ComponentManager;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.PersonNotFoundException;
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
    private final FilteredList<Entry> filteredPersons;

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
        filteredPersons = new FilteredList<>(taskManager.getPersons());
    }

    public ModelManager() {
        this(new TaskManager(), new UserPrefs());
    }

    public ModelManager(ReadOnlyTaskManager initialData, UserPrefs userPrefs) {
        taskManager = new TaskManager(initialData);
        filteredPersons = new FilteredList<>(taskManager.getPersons());
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
        raise(new AddressBookChangedEvent(taskManager));
    }

    @Override
    public synchronized void deleteTask(Entry target) throws PersonNotFoundException {
        taskManager.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addTask(Entry person) throws UniqueTaskList.DuplicateTaskException {
        taskManager.addTask(person);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void editTask(Update update)
            throws PersonNotFoundException, DuplicateTaskException {
        taskManager.editTask(update);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }
    
    @Override
    public void markTask(Entry task) throws PersonNotFoundException, DuplicateTaskException {
        taskManager.markTask(task);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }
    
    @Override
    public void unmarkTask(Entry task) throws PersonNotFoundException, DuplicateTaskException {
        taskManager.unmarkTask(task);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public void tagTask(Entry taskToTag, UniqueTagList tagsToAdd) {
        taskManager.tagTask(taskToTag, tagsToAdd);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public void untagTask(Entry taskToUntag, UniqueTagList tagsToRemove) throws PersonNotFoundException {
        taskManager.untagTask(taskToUntag, tagsToRemove);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public void addTag(Tag tag) throws DuplicateTagException {
        taskManager.addTag(tag);
    }
    //=========== Filtered Person List Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<Entry> getFilteredPersonList() {
        return new UnmodifiableObservableList<>(filteredPersons);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredPersons.setPredicate(null);
    }

    @Override
    public void updateFilteredEntryListPredicate(Predicate<Entry> predicate) {
        updateFilteredPersonList(predicate);
    }
    
    private void updateFilteredPersonList(Predicate<Entry> predicate) {
        filteredPersons.setPredicate(predicate);
    }

    

}
