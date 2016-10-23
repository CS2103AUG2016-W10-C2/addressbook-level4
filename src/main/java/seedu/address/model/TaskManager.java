package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.task.Task;
import seedu.address.model.task.Event;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniqueTaskList;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.EntryNotFoundException;
import seedu.address.model.task.Update;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.util.*;

/**
 * Wraps all data at the TaskManager level
 * Duplicates are not allowed (by .equals comparison)
 */
public class TaskManager implements ReadOnlyTaskManager {

    private final UniqueTaskList entries;
    private final UniqueTagList tags;

    {
        entries = new UniqueTaskList();
        tags = new UniqueTagList();
    }

    public TaskManager() {}

    /**
     * Persons and Tags are copied into this addressbook
     */
    public TaskManager(ReadOnlyTaskManager toBeCopied) {
        this(toBeCopied.getUniqueTaskList(), toBeCopied.getUniqueTagList());
    }

    /**
     * Persons and Tags are copied into this addressbook
     */
    public TaskManager(UniqueTaskList persons, UniqueTagList tags) {
        resetData(persons.getInternalList(), tags.getInternalList());
    }

    public static ReadOnlyTaskManager getEmptyAddressBook() {
        return new TaskManager();
    }

//// list overwrite operations

    public ObservableList<Entry> getEntries() {
        return entries.getInternalList();
    }

    public void setEntries(List<Entry> entries) {
        this.entries.getInternalList().setAll(entries);
    }

    public void setTags(Collection<Tag> tags) {
        this.tags.getInternalList().setAll(tags);
    }

    public void resetData(Collection<? extends Entry> newEntries, Collection<Tag> newTags) {
        ArrayList<Entry> copyList = new ArrayList<>();
        for (Entry entry : newEntries) {
            Entry copy;
            if (entry instanceof Event) {
                copy = new Event(entry);
            } else {
                copy = new Task(entry);
            }
            copyList.add(copy);
        }
        setEntries(copyList);
        setTags(newTags);
    }

    public void resetData(ReadOnlyTaskManager newData) {
        resetData(newData.getTaskList(), newData.getTagList());
    }

//// task-level operations

    /**
     * Adds a task to the address book.
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(Entry entry) throws UniqueTaskList.DuplicateTaskException {
        syncTagsWithMasterList(entry);
        entries.add(entry);
    }

    public void editTask(Update update)
            throws EntryNotFoundException, DuplicateTaskException {
        Entry toEdit = update.getTask();
        syncTagsWithMasterList(toEdit);
        entries.updateTitle(toEdit, update.getNewTitle());
        entries.updateTags(toEdit, update.getNewTags());
        entries.updateDescription(toEdit, update.getNewDescription());
    }

    public void markTask(Entry task) throws EntryNotFoundException {
        entries.mark(task);
    }

    public void unmarkTask(Entry task) throws EntryNotFoundException {
        entries.unmark(task);
    }


    /**
     * Ensures that every tag in this task:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    private void syncTagsWithMasterList(Entry entry) {
        final UniqueTagList personTags = entry.getTags();
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
        entry.setTags(new UniqueTagList(commonTagReferences));
    }

    public boolean removeEntry(Entry key) throws EntryNotFoundException {
        if (entries.remove(key)) {
            return true;
        } else {
            throw new EntryNotFoundException();
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
    public List<Entry> getTaskList() {
        return Collections.unmodifiableList(entries.getInternalList());
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags.getInternalList());
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        return this.entries;
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        return this.tags;
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskManager // instanceof handles nulls
                && this.entries.equals(((TaskManager) other).entries)
                && this.tags.equals(((TaskManager) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(entries, tags);
    }

}
