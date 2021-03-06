package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;

import static seedu.priorityq.ui.util.GuiUtil.OPAQUE;

/**
 * Provides a handle to the help window of the app.
 */
public class HelpWindowHandle extends GuiHandle {

    private static final String HELP_WINDOW_TITLE = "Help";
    private static final String HELP_WINDOW_ROOT_FIELD_ID = "#helpListView";

    public HelpWindowHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, HELP_WINDOW_TITLE);
    }

    public boolean isVisible() {
        return getNode(HELP_WINDOW_ROOT_FIELD_ID).getOpacity() == OPAQUE;
    }

    public void closeWindow() {
        super.closeWindow();
    }

}
