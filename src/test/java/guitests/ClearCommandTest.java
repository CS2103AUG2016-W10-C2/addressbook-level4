package guitests;

import org.junit.Test;
import seedu.priorityq.logic.commands.ClearCommand;
import seedu.priorityq.logic.commands.DeleteCommand;
import seedu.priorityq.testutil.TestEntry;
import seedu.priorityq.testutil.TypicalTestTasks;

import static org.junit.Assert.assertTrue;

public class ClearCommandTest extends TaskManagerGuiTest {

    @Test
    public void clear() {
        //verify a non-empty list can be cleared
        assertTrue(taskList.isListMatching(td.getTypicalSortedEntries()));
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        TestEntry testEntry = td.getTestEntry(TypicalTestTasks.BuyTasks.TASK_1);
        commandBox.runCommand(testEntry.getAddCommand());
        assertTrue(taskList.isListMatching(testEntry));
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertListSize(0);

        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertListSize(0);
        assertResultMessage(ClearCommand.MESSAGE_SUCCESS);
    }
}
