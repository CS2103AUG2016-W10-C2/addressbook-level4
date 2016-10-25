package guitests;

import guitests.guihandles.HelpWindowHandle;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HelpWindowTest extends AddressBookGuiTest {

    @Test
    public void openHelpWindow() {

        HelpWindowHandle helpWindowHandle = commandBox.runHelpCommand();
        assertHelpWindowOpen(helpWindowHandle);

        helpWindowHandle.pressEscape();
    }

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
    }
}
