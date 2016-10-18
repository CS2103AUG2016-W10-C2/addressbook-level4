package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.task.FloatingTask;
import seedu.address.model.task.Title;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniquePersonList;
import seedu.address.model.task.UniquePersonList.DuplicateTaskException;
import seedu.address.model.task.UniquePersonList.PersonNotFoundException;
import seedu.address.model.task.Update;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.util.*;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList entries;
    private final UniqueTagList tags;

    {
        entries = new UniquePersonList();
        tags = new UniqueTagList();
    }

    public AddressBook() {}

    /**
     * Persons and Tags are copied into this addressbook
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this(toBeCopied.getUniquePersonList(), toBeCopied.getUniqueTagList());
    }

    /**
     * Persons and Tags are copied into this addressbook
     */
    public AddressBook(UniquePersonList persons, UniqueTagList tags) {
        resetData(persons.getInternalList(), tags.getInternalList());
    }

    public static ReadOnlyAddressBook getEmptyAddressBook() {
        return new AddressBook();
    }

//// list overwrite operations

    public ObservableList<Entry> getPersons() {
        return entries.getInternalList();
    }

    public void setPersons(List<Entry> persons) {
        this.entries.getInternalList().setAll(persons);
    }

    public void setTags(Collection<Tag> tags) {
        this.tags.getInternalList().setAll(tags);
    }

    public void resetData(Collection<? extends Entry> newEntries, Collection<Tag> newTags) {
    	ArrayList<Entry> copyList = new ArrayList<>();
    	for (Entry entry : newEntries) {
    		Entry copy;
    		if (entry instanceof Deadline) {
    			copy = new Deadline(entry);
    		} else {
    			copy = new FloatingTask(entry);
    		}
    		copyList.add(copy);
    	}
        // setPersons(newEntries.stream().map(FloatingTask::new).collect(Collectors.toList()));
        setPersons(copyList);
        setTags(newTags);
    }

    public void resetData(ReadOnlyAddressBook newData) {
        resetData(newData.getPersonList(), newData.getTagList());
    }

//// task-level operations

    /**
     * Adds a task to the address book.
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniquePersonList.DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(Entry person) throws UniquePersonList.DuplicateTaskException {
        syncTagsWithMasterList(person);
        entries.add(person);
    }

    public void editTask(Update update)
            throws PersonNotFoundException, DuplicateTaskException {
        Entry toEdit = update.getTask();
        syncTagsWithMasterList(toEdit);
        entries.updateTitle(toEdit, update.getNewTitle());
        entries.updateTags(toEdit, update.getNewTags());
        entries.updateDescription(toEdit, update.getNewDescription());
    }
    
    public void markTask(Entry task) throws PersonNotFoundException, DuplicateTaskException {
        entries.mark(task);
    }
    
    public void unmarkTask(Entry task) throws PersonNotFoundException, DuplicateTaskException {
        entries.unmark(task);
    }


    /**
     * Ensures that every tag in this task:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    private void syncTagsWithMasterList(Entry person) {
        final UniqueTagList personTags = person.getTags();
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        for (Tag tag : tags) {
            masterTagObjects.put(tag, tag);
        }

        // Rebuild the list of task tags using references from the master list
        final Set<Tag> commonTagReferences = new HashSet<>();
        for (Tag tag : personTags) {
            commonTagReferences.add(masterTagObjects.get(tag));
        }
        person.setTags(new UniqueTagList(commonTagReferences));
    }

    public boolean removePerson(Entry key) throws UniquePersonList.PersonNotFoundException {
        if (entries.remove(key)) {
            return true;
        } else {
            throw new UniquePersonList.PersonNotFoundException();
        }
    }

//// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }
    

    public void tagTask(Entry taskToTag, UniqueTagList newTags) {
        taskToTag.addTags(newTags);
        syncTagsWithMasterList(taskToTag);
    }

    public void untagTask(Entry taskToUntag, UniqueTagList tagsToRemove) {
        taskToUntag.removeTags(tagsToRemove);
    }

//// util methods

    @Override
    public String toString() {
        return entries.getInternalList().size() + " persons, " + tags.getInternalList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public List<Entry> getPersonList() {
        return Collections.unmodifiableList(entries.getInternalList());
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags.getInternalList());
    }

    @Override
    public UniquePersonList getUniquePersonList() {
        return this.entries;
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        return this.tags;
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.entries.equals(((AddressBook) other).entries)
                && this.tags.equals(((AddressBook) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(entries, tags);
    }

}
