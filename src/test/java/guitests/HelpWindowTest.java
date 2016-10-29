package guitests;

import guitests.guihandles.HelpWindowHandle;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HelpWindowTest extends TaskManagerGuiTest {

    //@@author A0116603R
    @Test
    public void openAndCloseHelpWindow() {
        HelpWindowHandle helpWindowHandle = commandBox.runHelpCommand();
        assertHelpWindowOpen(helpWindowHandle);

        helpWindowHandle.pressEscape();
        assertHelpWindowClosed();
    }

    private void assertHelpWindowClosed() {
        assertTrue(taskList.isVisible());
    }

    // @@author
    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isVisible());
    }

}
