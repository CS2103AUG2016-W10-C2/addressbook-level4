package seedu.address.testutil;

import java.time.LocalDateTime;
import java.util.Arrays;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskManager;
import seedu.address.model.task.*;

public class TypicalTestTasks {

    public static TestEntry apple, banana, cat, doge, eggplant, study, jogging, homework, movie;

    public TypicalTestTasks() {
        try {
            apple =  new EntryBuilder().withTitle("Buy apples").withLastModifiedDate(LocalDateTime.parse("2016-10-10T10:00:00")).withTags("groceries").build();
            banana = new EntryBuilder().withTitle("Buy bananas").withLastModifiedDate(LocalDateTime.parse("2016-11-10T10:00:00")).withTags("groceries", "for scale").build();
            cat = new EntryBuilder().withTitle("Adopt a cat").withLastModifiedDate(LocalDateTime.parse("2016-10-10T10:00:00")).build();
            doge = new EntryBuilder().withTitle("Get blankets for doge").withLastModifiedDate(LocalDateTime.parse("2016-01-10T10:00:00")).build();
            eggplant = new EntryBuilder().withTitle("Buy eggplants").withLastModifiedDate(LocalDateTime.parse("2016-09-10T10:00:00")).build();
            study = new EntryBuilder().withTitle("Study for tests").withLastModifiedDate(LocalDateTime.parse("2016-12-10T10:00:00")).build();
            jogging = new EntryBuilder().withTitle("Go jogging this Thursday").withLastModifiedDate(LocalDateTime.parse("2016-10-10T10:00:00")).build();

            homework = new EntryBuilder().withTitle("Do assignment 314").withLastModifiedDate(LocalDateTime.parse("2015-10-10T10:00:00")).build();
            movie = new EntryBuilder().withTitle("Watch Deadpool").withLastModifiedDate(LocalDateTime.parse("2019-10-10T10:00:00")).build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(TaskManager taskManager) {

        try {
            taskManager.addTask(new Task(apple));
            taskManager.addTask(new Task(banana));
            taskManager.addTask(new Task(cat));
            taskManager.addTask(new Task(doge));
            taskManager.addTask(new Task(eggplant));
            taskManager.addTask(new Task(study));
            taskManager.addTask(new Task(jogging));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestEntry[] getTypicalSortedPersons() {
        TestEntry[] testEntry = new TestEntry[]{apple, banana, cat, doge, eggplant, study, jogging};
        Arrays.sort(testEntry, new EntryViewComparator());
        return testEntry;
    }

    public TaskManager getTypicalAddressBook(){
        TaskManager tm = new TaskManager();
        loadAddressBookWithSampleData(tm);
        return tm;
    }
}
