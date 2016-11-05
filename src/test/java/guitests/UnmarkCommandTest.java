package guitests;

import org.junit.Test;
import seedu.todolist.logic.commands.UnmarkCommand;
import seedu.todolist.model.task.Entry;

import static seedu.todolist.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;

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
