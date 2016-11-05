package seedu.todolist.ui;

import com.google.common.eventbus.Subscribe;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import seedu.todolist.commons.core.LogsCenter;
import seedu.todolist.commons.events.ui.ShowHelpListEvent;
import seedu.todolist.commons.events.ui.ShowTaskListEvent;

import java.util.logging.Logger;

//@@author A0116603R
/**
 * Controller which displays the help page
 */
public class HelpViewController extends Controller {
    private static final Logger logger = LogsCenter.getLogger(HelpViewController.class);

    @Override
    void init(Pane root) {
        initAppView();
        addToRootView(root);
        registerAsEventHandler(this);
    }

    void initAppView() {
        logger.info("Initialising HelpView...");
        appView = new BorderPane();
        HelpList helpList = new HelpList();
        helpList.init();
        appView.setCenter(helpList);
        appView.toBack();
        hide();
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpListEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        show();
    }

    @Subscribe
    private void handleTaskListEvent(ShowTaskListEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        hide();
    }

}
