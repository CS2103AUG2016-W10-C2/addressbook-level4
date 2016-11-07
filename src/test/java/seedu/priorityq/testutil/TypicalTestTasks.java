package seedu.priorityq.testutil;

import java.util.Arrays;

import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.TaskManager;
import seedu.priorityq.model.task.*;

import java.util.List;
import java.util.stream.Collectors;

//@@author A0116603R
/**
 * A class encapsulating typical tasks for test purposes.
 */
public class TypicalTestTasks {

    public static class BuyTasks implements TestTasks {
        static final String[] SAMPLES = new String[]{"Buy apples", "Buy bananas"};
        static final String[] NON_SAMPLES = new String[]{"Buy cookies", "Buy some time"};

        public static final String VERB = "Buy";

        static final String[] DEFAULT_TAGS = new String[]{"groceries", "shopping"};
        static final String DEFAULT_DESCRIPTION = "Gotta buy em' all";

        @Override
        public List<TestEntry> getSampleEntries() {
            return build(getAllEntries(SAMPLES));
        }

        @Override
        public List<TestEntry> getNonSampleEntries() {
            return build(getAllEntries(NON_SAMPLES));
        }

        @Override
        public List<EntryBuilder> getSampleEntriesWithTags() {
            return getAllEntries(SAMPLES).stream().map(entryBuilder -> {
                try {
                    return entryBuilder.withTags(DEFAULT_TAGS);
                } catch (IllegalValueException e) {
                    assert false : "not possible";
                }
                return null;
            }).collect(Collectors.toList());
        }

        @Override
        public List<EntryBuilder> getSampleEntriesWithDescription() {
            return getAllEntries(SAMPLES).stream().map(entryBuilder -> entryBuilder.withDescription(DEFAULT_DESCRIPTION)).collect(Collectors.toList());
        }

        public String getSampleEntryString() {
            return SAMPLES[0];
        }
    }

    public static class StudyTasks implements TestTasks {
        static final String[] SAMPLES = new String[]{"Study for finals", "Study for midterm"};
        static final String[] NON_SAMPLES = new String[]{"Read up on unit testing", "Code for project"};

        public static final String VERB = "Study";

        static final String DEFAULT_DESCRIPTION = "A short little description which grew longer";

        @Override
        public List<TestEntry> getSampleEntries() {
            return build(getAllEntries(SAMPLES));
        }

        @Override
        public List<TestEntry> getNonSampleEntries() {
            return build(getAllEntries(NON_SAMPLES));
        }

        @Override
        public List<EntryBuilder> getSampleEntriesWithTags() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("No StudyTasks with tags");
        }

        @Override
        public List<EntryBuilder> getSampleEntriesWithDescription() {
            return getAllEntries(SAMPLES).stream().map(entryBuilder -> entryBuilder.withDescription(DEFAULT_DESCRIPTION)).collect(Collectors.toList());
        }
    }

    public TaskManager getTypicalTaskManager(){
        TaskManager tm = new TaskManager();
        loadTaskManagerWithSampleData(tm);
        return tm;
    }

    public void loadTaskManagerWithSampleData(TaskManager taskManager) {
        for (Entry entry : getSampleEntries()) {
            try {
                taskManager.addTask(new Task(entry));
            } catch (UniqueTaskList.DuplicateTaskException e) {
                assert false : "not possible";
            }
        }
    }

    private List<TestEntry> getSampleEntries() {
        List<TestEntry> allTestEntries = new BuyTasks().getBuiltSampleEntriesWithTags();
        allTestEntries.addAll(new StudyTasks().getBuiltSampleEntriesWithDescription());
        return allTestEntries;
    }

    public TestEntry[] getTypicalSortedEntries() {
        TestEntry[] testEntry = getSampleEntriesAsArray();
        Arrays.sort(testEntry, new EntryViewComparator());
        return testEntry;
    }

    private TestEntry[] getSampleEntriesAsArray() {
        List<TestEntry> allTestEntries = getSampleEntries();
        TestEntry[] result = new TestEntry[allTestEntries.size()];
        return allTestEntries.toArray(result);
    }

    public List<TestEntry> getNonSampleEntries() {
        List<TestEntry> nonSampleEntries = new BuyTasks().getNonSampleEntries();
        nonSampleEntries.addAll(new StudyTasks().getNonSampleEntries());
        return nonSampleEntries;
    }

    public TestEntry getTestEntry(String title) {
        TestEntry entry = null;
        try {
            entry = new EntryBuilder().withTitle(title).build();
        } catch (IllegalValueException e) {
            assert false : "not possible";
        }
        return entry;
    }
}
