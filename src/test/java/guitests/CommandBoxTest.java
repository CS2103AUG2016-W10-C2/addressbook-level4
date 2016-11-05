package guitests;

import org.junit.Test;
import seedu.priorityq.testutil.TestEntry;
import seedu.priorityq.testutil.TypicalTestTasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandBoxTest extends TaskManagerGuiTest {

    @Test
    public void commandBox_commandSucceeds_textCleared() {
        TestEntry testEntry = td.getTestEntry(TypicalTestTasks.BuyTasks.TASK_1);
        commandBox.runCommand(testEntry.getAddCommand());
        assertEquals(commandBox.getCommandInput(), "");
    }

    @Test
    public void commandBox_commandFails_textStays(){
        commandBox.runCommand("invalid command text remains");
        assertEquals(commandBox.getCommandInput(), "invalid command text remains");
    }

    //@@author A0116603R
    @Test
    public void commandBox_commandFails_redBorder() {
        commandBox.runCommand("invalid command produces error styling");
        assertTrue(commandBox.hasErrorClass());
    }

}
