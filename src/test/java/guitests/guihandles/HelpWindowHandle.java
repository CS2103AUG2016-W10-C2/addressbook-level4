package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;

import static seedu.address.ui.util.GuiUtil.OPAQUE;
import static seedu.address.ui.util.GuiUtil.TRANSPARENT;

/**
 * Provides a handle to the help window of the app.
 */
public class HelpWindowHandle extends GuiHandle {

    private static final String HELP_WINDOW_TITLE = "Help";
    private static final String HELP_WINDOW_ROOT_FIELD_ID = "#helpListView";

    public HelpWindowHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, HELP_WINDOW_TITLE);
        guiRobot.sleep(300);
    }

    public boolean isVisible() {
        return getNode(HELP_WINDOW_ROOT_FIELD_ID).getOpacity() == OPAQUE;
    }

    public void closeWindow() {
        super.closeWindow();
        guiRobot.sleep(500);
    }

}
