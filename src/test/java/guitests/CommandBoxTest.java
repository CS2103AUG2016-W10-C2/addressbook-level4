package guitests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommandBoxTest extends AddressBookGuiTest {

    @Test
    public void commandBox_commandSucceeds_textCleared() {
        commandBox.runCommand(td.banana.getAddCommand());
        assertEquals(commandBox.getCommandInput(), "");
    }

    @Test
    public void commandBox_commandFails_textStays(){
        commandBox.runCommand("invalid command text remains");
        assertEquals(commandBox.getCommandInput(), "invalid command text remains");
    }

    @Test
    public void commandBox_commandFails_redBorder() {
        commandBox.runCommand("invalid command produces red glow");
        assertTrue(commandBox.hasErrorClass());
    }

}
