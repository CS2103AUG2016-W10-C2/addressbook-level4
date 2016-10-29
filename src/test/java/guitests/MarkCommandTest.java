package guitests;

import org.junit.Test;
import seedu.address.logic.commands.MarkCommand;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;

public class MarkCommandTest extends TaskManagerGuiTest {
    @Test
    public void markFloatingTask() {
        int testIndex = 1;
        commandBox.runCommand(MarkCommand.COMMAND_WORD + " " + testIndex);
        assertResultMessage(String.format(MarkCommand.MESSAGE_SUCCESS, taskList.getEntry(testIndex)));

        commandBox.runCommand(MarkCommand.COMMAND_WORD + " " + Integer.MAX_VALUE);
        assertResultMessage(MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }
}
