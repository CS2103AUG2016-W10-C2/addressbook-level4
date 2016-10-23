package seedu.address.ui;

import com.google.common.eventbus.Subscribe;
import javafx.scene.layout.BorderPane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.ShowHelpListEvent;
import seedu.address.commons.events.ui.ShowTaskListEvent;

import java.util.logging.Logger;

public class HelpViewController extends Controller {
    private static final Logger logger = LogsCenter.getLogger(HelpViewController.class);

    @Override
    void init() {
        initAppView();
        addToRootView();
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
