package guitests;

import org.junit.Test;
import java.util.Arrays;

import seedu.priorityq.commons.core.Messages;
import seedu.priorityq.logic.commands.ClearCommand;
import seedu.priorityq.logic.commands.DeleteCommand;
import seedu.priorityq.logic.commands.ListCommand;
import seedu.priorityq.model.task.EntryViewComparator;
import seedu.priorityq.testutil.TestEntry;
import seedu.priorityq.testutil.TestTasks;
import seedu.priorityq.testutil.TypicalTestTasks;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ListCommandTest extends TaskManagerGuiTest {

    //@@author A0116603R-reused
    @Test
    public void list_nonEmptyList() {
        // search miss
        assertListResult(ListCommand.COMMAND_WORD + " 404 doge not found");

        // search hits for BuyTasks
        TestTasks generator = new TypicalTestTasks.BuyTasks();
        assertListResult(ListCommand.COMMAND_WORD + " " + TypicalTestTasks.BuyTasks.VERB, generator.getSampleEntries());

        // search after deleting one result
        commandBox.runCommand(DeleteCommand.COMMAND_WORD + " 1");
        generator = new TypicalTestTasks.StudyTasks();
        assertListResult(ListCommand.COMMAND_WORD + " " + TypicalTestTasks.StudyTasks.VERB, generator.getSampleEntries());
    }

    @Test
    public void list_emptyList(){
        // clear the list and assert search miss
        commandBox.runCommand(ClearCommand.COMMAND_WORD);
        assertListResult(ListCommand.COMMAND_WORD + " " + TypicalTestTasks.BuyTasks.VERB);
    }

    @Test
    public void list_invalidCommand_fail() {
        commandBox.runCommand("listdoge");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    private void assertListResult(String command, TestEntry... expectedHits ) {
        Arrays.sort(expectedHits, new EntryViewComparator());
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " entries listed!");
        assertTrue(taskList.isListMatching(expectedHits));
    }

    private void assertListResult(String command, List<TestEntry> expectedHits) {
        TestEntry[] array = new TestEntry[expectedHits.size()];
        expectedHits.toArray(array);
        assertListResult(command, array);
    }
}
