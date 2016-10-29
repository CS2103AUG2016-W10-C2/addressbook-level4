package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskManager;
import seedu.address.model.task.*;

import java.util.List;
import java.util.Set;

//@@author A0116603R
/**
 * A class encapsulating typical tasks for test purposes.
 */
public class TypicalTestTasks {

    public static class BuyTasks implements TestTasks {
        public static final String TASK_1 = "Buy apples";
        static final String TASK_2 = "Buy bananas";
        static final String TASK_3 = "Buy cookies";
        static final String TASK_4 = "Buy some time";

        public static final String VERB = "Buy";

        @Override
        public List<TestEntry> getSampleEntries() {
            return getAllEntries(TASK_1, TASK_2);
        }

        @Override
        public List<TestEntry> getNonSampleEntries() {
            return getAllEntries(TASK_3, TASK_4);
        }
    }

    private static class StudyTasks implements TestTasks {
        static final String TASK_1 = "Study for finals";
        static final String TASK_2 = "Do assignment 1";
        static final String TASK_3 = "Read up on unit testing";
        static final String TASK_4 = "Code for project";

        @Override
        public List<TestEntry> getSampleEntries() {
            return getAllEntries(TASK_1, TASK_2);
        }

        @Override
        public List<TestEntry> getNonSampleEntries() {
            return getAllEntries(TASK_3, TASK_4);
        }
    }

    public static class WatchTasks implements TestTasks {
        static final String TASK_1 = "Watch movie";
        static final String TASK_2 = "Watch Black Mirror";

        public static final String VERB = "Watch";

        @Override
        public List<TestEntry> getSampleEntries() {
            return getAllEntries(TASK_1);
        }

        @Override
        public List<TestEntry> getNonSampleEntries() {
            return getAllEntries(TASK_2);
        }
    }

    public static void loadAddressBookWithSampleData(TaskManager taskManager) {
        for (Entry entry : getSampleEntries()) {
            try {
                taskManager.addTask(new Task(entry));
            } catch (UniqueTaskList.DuplicateTaskException e) {
                assert false : "not possible";
            }
        }
    }

    private static List<TestEntry> getSampleEntries() {
        List<TestEntry> allTestEntries = new BuyTasks().getSampleEntries();
        allTestEntries.addAll(new StudyTasks().getSampleEntries());
        allTestEntries.addAll(new WatchTasks().getSampleEntries());
        return allTestEntries;
    }

    public TestEntry[] getSampleEntriesAsArray() {
        List<TestEntry> allTestEntries = getSampleEntries();
        TestEntry[] result = new TestEntry[allTestEntries.size()];
        return allTestEntries.toArray(result);
    }

    public List<TestEntry> getNonSampleEntries() {
        List<TestEntry> nonSampleEntries = new BuyTasks().getNonSampleEntries();
        nonSampleEntries.addAll(new StudyTasks().getNonSampleEntries());
        nonSampleEntries.addAll(new WatchTasks().getNonSampleEntries());
        return nonSampleEntries;
    }

    public TaskManager getTypicalTaskManager(){
        TaskManager tm = new TaskManager();
        loadAddressBookWithSampleData(tm);
        return tm;
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
