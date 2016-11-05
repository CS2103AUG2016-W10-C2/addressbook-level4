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

    default List<TestEntry> build(List<EntryBuilder> entryBuilders) {
        if (entryBuilders == null) return null;
        return entryBuilders.stream().map(EntryBuilder::build).sorted(new EntryViewComparator()).collect(Collectors.toList());
    }

    /**
     * Returns the entries used as sample data for this category of tasks
     */
    List<TestEntry> getSampleEntries();

    /**
     * Returns the entries used as new data that can be arbitrarily added to
     * a test task manager instance
     */
    List<TestEntry> getNonSampleEntries();

    List<EntryBuilder> getSampleEntriesWithTags();
    List<EntryBuilder> getSampleEntriesWithDescription();

    default List<TestEntry> getBuiltSampleEntriesWithDescription() {
        return build(getSampleEntriesWithDescription());
    }

    default List<TestEntry> getBuiltSampleEntriesWithTags() {
        return build(getSampleEntriesWithTags());
    }
}
