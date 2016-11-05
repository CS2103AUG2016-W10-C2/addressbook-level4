package seedu.address.logic.commands;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @@author A0127828W
 */
public class CommandHistoryTest {
    private CommandHistory history;

    @Before
    public void setup() {
        history = new CommandHistory();
    }

    @Test
    public void appendCommand_emptyString_null() throws Exception {
        history.appendCommand(null);
        assertEquals(history.getPreviousCommand(), null);

        history.appendCommand("");
        assertEquals(history.getPreviousCommand(), null);
    }

    @Test
    public void getPreviousCommand_String_Success() throws Exception {
        commandSimulator("add 1");
        assertEquals("add 1", history.getPreviousCommand());
        history = new CommandHistory();
    }

    @Test
    public void getPreviousCommand_Empty_null() throws Exception {
        assertEquals(history.getPreviousCommand(), null);
    }

    @Test
    public void getPreviousCommand_Multiple() throws Exception {
        commandSimulator("add 1");
        commandSimulator("add 2");
        commandSimulator("add 3");
        assertEquals("add 3", history.getPreviousCommand());
        assertEquals("add 2", history.getPreviousCommand());
        assertEquals("add 1", history.getPreviousCommand());
        assertEquals(null, history.getPreviousCommand());
        history = new CommandHistory();
    }

    @Test
    public void getNextCommand_Empty_emptyString() throws Exception {
        assertEquals("", history.getNextCommand());
    }

    @Test
    public void getNextCommand_Multiple() throws Exception {
        commandSimulator("add 1");
        commandSimulator("add 2");

        history.getPreviousCommand();
        history.getPreviousCommand();
        history.getPreviousCommand();

        assertEquals("add 2", history.getNextCommand());
        assertEquals("", history.getNextCommand());

        history = new CommandHistory();
    }

    @Test
    public void resetPosition() throws Exception {
        commandSimulator("add 1");
        commandSimulator("add 2");
        commandSimulator("add 3");

        assertEquals("add 3", history.getPreviousCommand());
        history.resetPosition();
        assertEquals("add 3", history.getPreviousCommand());
        history = new CommandHistory();
    }

    /**
     * Append the command to command history
     * Simulate the behavior when a command is typed in from the command box
     */
    private void commandSimulator(String command) {
        history.appendCommand(command);
        history.resetPosition();
    }
}
