package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;
import java.util.Arrays;

import seedu.priorityq.logic.commands.AddCommand;
import seedu.priorityq.model.task.Entry;
import seedu.priorityq.model.task.EntryViewComparator;
import seedu.priorityq.commons.core.Messages;
import seedu.priorityq.logic.commands.ClearCommand;
import seedu.priorityq.testutil.TestEntry;
import seedu.priorityq.testutil.TestUtil;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends TaskManagerGuiTest {

    //@@author A0116603R-reused
    @Test
    public void add() {
        TestEntry[] currentList = td.getTypicalSortedPersons(); // sample entries already present
        TestEntry testEntry;

        //add new entries
        for (TestEntry entry : td.getNonSampleEntries()) {
            testEntry = entry;
            assertAddSuccess(testEntry, currentList);
            currentList = TestUtil.addPersonsToList(currentList, testEntry);
        }

        assertTrue(currentList.length > 0);

        //add duplicate task
        commandBox.runCommand(currentList[0].getAddCommand());
        assertResultMessage(AddCommand.MESSAGE_DUPLICATE_ENTRY);
        assertTrue(taskList.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertAddSuccess(currentList[0]);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);

    }

    //@@author
    private void assertAddSuccess(TestEntry testEntry, TestEntry... currentList) {
        commandBox.runCommand(testEntry.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskList.navigateToEntry(testEntry.getTitle().fullTitle);
        assertMatching(testEntry, addedCard);
        
        Entry entry = taskList.getEntry(taskList.getTaskIndex(testEntry));
        testEntry.setLastModifiedTime(entry.getLastModifiedTime());

        //confirm the list now contains all previous persons plus the new task
        TestEntry[] expectedList = TestUtil.addPersonsToList(currentList, testEntry);
        Arrays.sort(expectedList, new EntryViewComparator());
        assertTrue(taskList.isListMatching(expectedList));
    }

}
