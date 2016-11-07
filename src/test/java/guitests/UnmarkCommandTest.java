package guitests;

import org.junit.Test;
import seedu.priorityq.logic.commands.UnmarkCommand;
import seedu.priorityq.model.entry.Entry;

import static seedu.priorityq.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;

//@@author A0116603R
public class UnmarkCommandTest extends TaskManagerGuiTest{
    @Test
    public void unmarkFloatingTask() {
        int testIndex = 0;
        Entry entry = taskList.getEntry(testIndex);
        commandBox.runCommand(UnmarkCommand.COMMAND_WORD + " " + testIndex+1); // gui uses 1-based indexing
        assertResultMessage(String.format(UnmarkCommand.MESSAGE_SUCCESS, entry));

        commandBox.runCommand(UnmarkCommand.COMMAND_WORD + " " + Integer.MAX_VALUE);
        assertResultMessage(MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }
}
