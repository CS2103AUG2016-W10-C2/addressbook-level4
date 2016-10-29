package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;

import java.util.ArrayList;
import java.util.List;

//@@author A0116603R
/**
 * An interface representing a category of typical test tasks.
 */
public interface TestTasks {
    default List<TestEntry> getAllEntries(String... tasks) {
        List<TestEntry> entries = new ArrayList<>();
        for (String task : tasks) {
            try {
                entries.add(new EntryBuilder().withTitle(task).build());
            } catch (IllegalValueException e) {
                assert false : "not possible";
            }
        }
        return entries;
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
}
