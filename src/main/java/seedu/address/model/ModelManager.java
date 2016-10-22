package seedu.address.model;

import javafx.collections.transformation.FilteredList;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.util.StringUtil;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.core.ComponentManager;
import seedu.address.model.task.Entry;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Task;
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
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final FilteredList<Entry> filteredPersons;

    /**
     * Initializes a ModelManager with the given AddressBook
     * AddressBook and its variables should not be null
     */
    public ModelManager(AddressBook src, UserPrefs userPrefs) {
        super();
        assert src != null;
        assert userPrefs != null;

        logger.fine("Initializing with address book: " + src + " and user prefs " + userPrefs);

        addressBook = new AddressBook(src);
        filteredPersons = new FilteredList<>(addressBook.getPersons());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    public ModelManager(ReadOnlyAddressBook initialData, UserPrefs userPrefs) {
        addressBook = new AddressBook(initialData);
        filteredPersons = new FilteredList<>(addressBook.getPersons());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deleteTask(Entry target) throws PersonNotFoundException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addTask(Entry person) throws UniquePersonList.DuplicateTaskException {
        addressBook.addTask(person);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void editTask(Update update)
            throws PersonNotFoundException, DuplicateTaskException {
        addressBook.editTask(update);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }
    
    @Override
    public void markTask(Entry task) throws PersonNotFoundException, DuplicateTaskException {
        addressBook.markTask(task);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }
    
    @Override
    public void unmarkTask(Entry task) throws PersonNotFoundException, DuplicateTaskException {
        addressBook.unmarkTask(task);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public void tagTask(Entry taskToTag, UniqueTagList tagsToAdd) {
        addressBook.tagTask(taskToTag, tagsToAdd);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public void untagTask(Entry taskToUntag, UniqueTagList tagsToRemove) throws PersonNotFoundException {
        addressBook.untagTask(taskToUntag, tagsToRemove);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public void addTag(Tag tag) throws DuplicateTagException {
        addressBook.addTag(tag);
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
