package guitests;

import org.junit.Test;
import seedu.address.logic.commands.UnmarkCommand;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;

public class UnmarkCommandTest extends TaskManagerGuiTest{
    @Test
    public void unmarkFloatingTask() {
        int testIndex = 1;
        commandBox.runCommand(UnmarkCommand.COMMAND_WORD + " " + testIndex);
        assertResultMessage(String.format(UnmarkCommand.MESSAGE_SUCCESS, taskList.getEntry(testIndex)));

        commandBox.runCommand(UnmarkCommand.COMMAND_WORD + " " + Integer.MAX_VALUE);
        assertResultMessage(MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }
}
