package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.TaskManager;
import seedu.address.model.task.*;

/**
 *
 */
public class TypicalTestPersons {

    public static TestEntry alice, benson, carl, daniel, elle, fiona, george, hoon, ida;

    public TypicalTestPersons() {
        try {
            alice =  new EntryBuilder().withTitle("Alice Pauline").withTags("friends").build();
            benson = new EntryBuilder().withTitle("Benson Meier").withTags("owesMoney", "friends").build();
            carl = new EntryBuilder().withTitle("Carl Kurz").build();
            daniel = new EntryBuilder().withTitle("Daniel Meier").build();
            elle = new EntryBuilder().withTitle("Elle Meyer").build();
            fiona = new EntryBuilder().withTitle("Fiona Kunz").build();
            george = new EntryBuilder().withTitle("George Best").build();

            //Manually added
            hoon = new EntryBuilder().withTitle("Hoon Meier").build();
            ida = new EntryBuilder().withTitle("Ida Mueller").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(TaskManager ab) {

        try {
            ab.addTask(new Task(alice));
            ab.addTask(new Task(benson));
            ab.addTask(new Task(carl));
            ab.addTask(new Task(daniel));
            ab.addTask(new Task(elle));
            ab.addTask(new Task(fiona));
            ab.addTask(new Task(george));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestEntry[] getTypicalPersons() {
        return new TestEntry[]{alice, benson, carl, daniel, elle, fiona, george};
    }

    public TaskManager getTypicalAddressBook(){
        TaskManager ab = new TaskManager();
        loadAddressBookWithSampleData(ab);
        return ab;
    }
}
