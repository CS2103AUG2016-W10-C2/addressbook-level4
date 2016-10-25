package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskManager;
import seedu.address.model.task.*;

public class TypicalTestTasks {

    public static TestEntry apple, banana, cat, doge, eggplant, study, jogging, homework, movie;

    public TypicalTestTasks() {
        try {
            apple =  new EntryBuilder().withTitle("Buy apples").withTags("groceries").build();
            banana = new EntryBuilder().withTitle("Buy bananas").withTags("groceries", "for scale").build();
            cat = new EntryBuilder().withTitle("Adopt a cat").build();
            doge = new EntryBuilder().withTitle("Get blankets for doge").build();
            eggplant = new EntryBuilder().withTitle("Buy eggplants").build();
            study = new EntryBuilder().withTitle("Study for tests").build();
            jogging = new EntryBuilder().withTitle("Go jogging this Thursday").build();

            homework = new EntryBuilder().withTitle("Do assignment 314").build();
            movie = new EntryBuilder().withTitle("Watch Deadpool").build();
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

    public TestEntry[] getTypicalPersons() {
        return new TestEntry[]{apple, banana, cat, doge, eggplant, study, jogging};
    }

    public TaskManager getTypicalAddressBook(){
        TaskManager tm = new TaskManager();
        loadAddressBookWithSampleData(tm);
        return tm;
    }
}
