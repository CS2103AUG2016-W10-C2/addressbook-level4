package guitests;

import org.junit.Test;
import seedu.priorityq.commons.core.Messages;
import seedu.priorityq.testutil.TestEntry;
import seedu.priorityq.testutil.TestUtil;

import static org.junit.Assert.assertTrue;
import static seedu.priorityq.logic.commands.DeleteCommand.MESSAGE_DELETE_ENTRY_SUCCESS;

public class DeleteCommandTest extends TaskManagerGuiTest {

    @Test
    public void delete() {

        //delete the first in the list
        TestEntry[] currentList = td.getTypicalSortedEntries();
        int targetIndex = 1;
        assertDeleteSuccess(targetIndex, currentList);

        //delete the last in the list
        currentList = TestUtil.removeEntryFromList(currentList, targetIndex);
        targetIndex = currentList.length;
        assertDeleteSuccess(targetIndex, currentList);

        //delete from the middle of the list
        currentList = TestUtil.removeEntryFromList(currentList, targetIndex);
        targetIndex = currentList.length/2;
        assertDeleteSuccess(targetIndex, currentList);

        //invalid index
        commandBox.runCommand("delete " + currentList.length + 1);
        assertResultMessage(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);

    }

    /**
     * Runs the delete command to delete the task at specified index and confirms the result is correct.
     * @param targetIndexOneIndexed e.g. to delete the first task in the list, 1 should be given as the target index.
     * @param currentList A copy of the current list of entries (before deletion).
     */
    private void assertDeleteSuccess(int targetIndexOneIndexed, final TestEntry[] currentList) {
        TestEntry entryToDelete = currentList[targetIndexOneIndexed-1]; //-1 because array uses zero indexing
        TestEntry[] expectedRemainder = TestUtil.removeEntryFromList(currentList, targetIndexOneIndexed);

        commandBox.runCommand("delete " + targetIndexOneIndexed);

        //confirm the list now contains all previous entries except the deleted task
        assertTrue(taskList.isListMatching(expectedRemainder));

        //confirm the result message is correct
        assertResultMessage(String.format(MESSAGE_DELETE_ENTRY_SUCCESS, entryToDelete));
    }

}
