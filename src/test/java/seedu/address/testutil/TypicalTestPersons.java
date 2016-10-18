package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.task.*;

/**
 *
 */
public class TypicalTestPersons {

    public static TestEntry alice, benson, carl, daniel, elle, fiona, george, hoon, ida;

    public TypicalTestPersons() {
        try {
            alice =  new PersonBuilder().withName("Alice Pauline").withTags("friends").build();
            benson = new PersonBuilder().withName("Benson Meier").withTags("owesMoney", "friends").build();
            carl = new PersonBuilder().withName("Carl Kurz").build();
            daniel = new PersonBuilder().withName("Daniel Meier").build();
            elle = new PersonBuilder().withName("Elle Meyer").build();
            fiona = new PersonBuilder().withName("Fiona Kunz").build();
            george = new PersonBuilder().withName("George Best").build();

            //Manually added
            hoon = new PersonBuilder().withName("Hoon Meier").build();
            ida = new PersonBuilder().withName("Ida Mueller").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(AddressBook ab) {

        try {
            ab.addTask(new FloatingTask(alice));
            ab.addTask(new FloatingTask(benson));
            ab.addTask(new FloatingTask(carl));
            ab.addTask(new FloatingTask(daniel));
            ab.addTask(new FloatingTask(elle));
            ab.addTask(new FloatingTask(fiona));
            ab.addTask(new FloatingTask(george));
        } catch (UniquePersonList.DuplicateTaskException e) {
            assert false : "not possible";
        }
    }

    public TestEntry[] getTypicalPersons() {
        return new TestEntry[]{alice, benson, carl, daniel, elle, fiona, george};
    }

    public AddressBook getTypicalAddressBook(){
        AddressBook ab = new AddressBook();
        loadAddressBookWithSampleData(ab);
        return ab;
    }
}
