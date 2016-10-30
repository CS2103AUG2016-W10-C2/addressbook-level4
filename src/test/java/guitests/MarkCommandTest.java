package guitests;

import guitests.guihandles.TaskCardHandle;
import org.junit.Test;
import seedu.address.commons.events.ui.DidMarkTaskEvent;
import seedu.address.commons.events.ui.MarkTaskEvent;
import seedu.address.logic.commands.MarkCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.UnmarkCommand;
import seedu.address.testutil.EventsCollector;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX;

public class MarkCommandTest extends TaskManagerGuiTest {

    private int testIndex = 0;

    @Test
    public void markFloatingTask_viaCommandBox() {
        markTask(testIndex+1);
        assertResultMessage(String.format(MarkCommand.MESSAGE_SUCCESS, taskList.getEntry(testIndex)));

        markTask(Integer.MAX_VALUE);
        assertResultMessage(MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
    }

    @Test
    public void markFloatingTask_viaGuiClick() {
        TaskCardHandle tch = taskList.getTaskCardHandle(testIndex);

        EventsCollector collector = new EventsCollector();
        String expectedMsg = tch.getIsMarked() ? UnmarkCommand.MESSAGE_SUCCESS : MarkCommand.MESSAGE_SUCCESS;
        tch.toggleCheckBox();
        assertTrue(collector.get(0) instanceof MarkTaskEvent);
        assertResultMessage(String.format(expectedMsg, taskList.getEntry(testIndex)));
    }

    @Test
    public void markFloatingTaskAndUndo() {
        TaskCardHandle tch = taskList.getTaskCardHandle(testIndex);
        boolean currState = tch.getIsMarked();
        markTask(testIndex+1);
        assertTrue(currState != tch.getIsMarked());
        commandBox.runCommand(UndoCommand.COMMAND_WORD);
        assertTrue(currState == tch.getIsMarked());
    }

    // Run the mark command in the command box. Note that the gui uses 1-based indexing.
    private void markTask(int testIndex) {
        commandBox.runCommand(MarkCommand.COMMAND_WORD + " " + testIndex);
    }
}
