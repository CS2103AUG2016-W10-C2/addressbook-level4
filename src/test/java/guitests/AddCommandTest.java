package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;
import java.util.Arrays;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.task.Entry;
import seedu.address.model.task.EntryViewComparator;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestEntry;
import seedu.address.testutil.TestUtil;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends AddressBookGuiTest {

    @Test
    public void add() {
        //add one task
        TestEntry[] currentList = td.getTypicalSortedPersons();
        TestEntry testEntry = td.homework;
        assertAddSuccess(testEntry, currentList);
        currentList = TestUtil.addPersonsToSortedList(currentList, testEntry);

        //add another task
        testEntry = td.movie;
        assertAddSuccess(testEntry, currentList);
        currentList = TestUtil.addPersonsToSortedList(currentList, testEntry);

        //add duplicate task
        commandBox.runCommand(td.homework.getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_ENTRY);
        assertTrue(taskList.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand("clear");
        assertAddSuccess(td.apple);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

    }

    private void assertAddSuccess(TestEntry testEntry, TestEntry... currentList) {
        commandBox.runCommand(testEntry.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskList.navigateToPerson(testEntry.getTitle().fullTitle);
        assertMatching(testEntry, addedCard);
        
        Entry entry = taskList.getPerson(taskList.getPersonIndex(testEntry));
        testEntry.setLastModifiedTime(entry.getLastModifiedTime());

        //confirm the list now contains all previous persons plus the new task
        TestEntry[] expectedList = TestUtil.addPersonsToList(currentList, testEntry);
        Arrays.sort(expectedList, new EntryViewComparator());
        assertTrue(taskList.isListMatching(expectedList));
    }

}
