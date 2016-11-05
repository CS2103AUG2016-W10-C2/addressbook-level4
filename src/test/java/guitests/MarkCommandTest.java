package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;
import seedu.priorityq.logic.commands.MarkCommand;
import seedu.priorityq.logic.commands.UndoCommand;
import seedu.priorityq.logic.commands.UnmarkCommand;
import seedu.priorityq.model.task.Entry;

import static org.junit.Assert.assertTrue;
import static seedu.priorityq.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;

//@@author A0116603R
public class MarkCommandTest extends TaskManagerGuiTest {

    private int testIndex = 0;

    @Test
    public void markFloatingTask_viaCommandBox() {
        Entry entry = taskList.getEntry(testIndex);
        boolean isMarked = entry.isMarked();
        markTask(testIndex+1);
        assertTrue(isMarked != entry.isMarked());

        markTask(Integer.MAX_VALUE);
        assertResultMessage(MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void markFloatingTask_viaGuiClick() {
        Entry entry = taskList.getEntry(testIndex);
        TaskCardHandle tch = taskList.getTaskCardHandle(testIndex);

        String expectedMsg = tch.getIsMarked() ? UnmarkCommand.MESSAGE_SUCCESS : MarkCommand.MESSAGE_SUCCESS;
        tch.toggleCheckBox();
        assertResultMessage(String.format(expectedMsg, entry));
    }

    @Test
    public void markFloatingTaskAndUndo() {
        Entry entry = taskList.getEntry(testIndex);
        TaskCardHandle tch = taskList.getTaskCardHandle(testIndex);
        boolean currState = tch.getIsMarked();
        markTask(testIndex+1);
        assertTrue(currState != entry.isMarked());
        commandBox.runCommand(UndoCommand.COMMAND_WORD);
        assertTrue(currState == entry.isMarked());
    }

    // Run the mark command in the command box. Note that the gui uses 1-based indexing.
    private void markTask(int testIndex) {
        commandBox.runCommand(MarkCommand.COMMAND_WORD + " " + testIndex);
    }
}
