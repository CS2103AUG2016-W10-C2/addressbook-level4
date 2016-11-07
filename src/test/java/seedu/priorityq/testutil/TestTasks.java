package seedu.priorityq.testutil;

import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.task.EntryViewComparator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@@author A0116603R
/**
 * An interface representing a category of typical test tasks.
 */
public interface TestTasks {

    /**
     * Returns a list of EntryBuilders generated from the list of Strings passed in.
     * @param tasks The title of the entries to be created
     * @return a list of EntryBuilders created from the specified titles
     */
    default List<EntryBuilder> getAllEntries(String... tasks) {
        List<EntryBuilder> entries = new ArrayList<>();
        for (String task : tasks) {
            try {
                entries.add(new EntryBuilder().withTitle(task).withLastModifiedDate(LocalDateTime.now()));
            } catch (IllegalValueException e) {
                assert false : "not possible";
            }
        }
        return entries;
    }

    /**
     * Builds all of the EntryBuilders passed in.
     */
    default List<TestEntry> build(List<EntryBuilder> entryBuilders) {
        if (entryBuilders == null) return null;
        return entryBuilders.stream().map(EntryBuilder::build).sorted(new EntryViewComparator()).collect(Collectors.toList());
    }

    /**
     * Returns the entries used as sample data for this category of tasks.
     */
    List<TestEntry> getSampleEntries();

    /**
     * Returns the entries used as new data that can be arbitrarily added to
     * a test task manager instance.
     * This is an OPTIONAL method.
     */
    List<TestEntry> getNonSampleEntries();

    /**
     * Returns the entries used as sample data, with tags.
     * This is an OPTIONAL method.
     */
    List<EntryBuilder> getSampleEntriesWithTags() throws UnsupportedOperationException;

    /**
     * Returns the entries used as sample data, with descriptions.
     * This is an OPTIONAL method.
     */
    List<EntryBuilder> getSampleEntriesWithDescription();

    /**
     * Returns the TestEntries built from the sample entries which have descriptions.
     */
    default List<TestEntry> getBuiltSampleEntriesWithDescription() {
        return build(getSampleEntriesWithDescription());
    }

    /**
     * Returns the TestEntries built from the sample entries which have tags.
     */
    default List<TestEntry> getBuiltSampleEntriesWithTags() {
        try {
            return build(getSampleEntriesWithTags());
        } catch (UnsupportedOperationException uoe) {
            return null;
        }
    }
}
