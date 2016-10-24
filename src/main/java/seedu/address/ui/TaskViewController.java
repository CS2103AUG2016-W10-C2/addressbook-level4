package seedu.address.ui;

import com.google.common.eventbus.Subscribe;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.ShowHelpListEvent;
import seedu.address.commons.events.ui.ShowTaskListEvent;
import seedu.address.logic.Logic;
import seedu.address.model.task.Entry;

import java.util.logging.Logger;

/**
 * Controller which initializes the default Task View -- this consists of
 * a TaskList and a CommandArea
 */
//@@author A0116603R
public class TaskViewController extends Controller {
    private static final Logger logger = LogsCenter.getLogger(TaskViewController.class);

    private Logic logic;

    TaskViewController(Logic logic) {
        this.logic = logic;
    }

    // ################
    // # INITIALISERS #
    // ################
    @Override
    void init(Pane root) {
        initAppView();
        addToRootView(root);
        registerAsEventHandler(this);
    }

    void initAppView() {
        logger.info("Initialising TaskView...");
        appView = new BorderPane();
        TaskList taskList = new TaskList();
        taskList.init(getDefaultTasks());
        appView.setCenter(taskList);
        appView.setBottom(new CommandArea(logic));
        appView.toFront();
    }

    // ###################
    // # PRIVATE GETTERS #
    // ###################

    private ObservableList<Entry> getDefaultTasks() {
        assert logic != null;
        return logic.getFilteredPersonList();
    }

    @Subscribe
    private void handleShowHelpEvent(ShowHelpListEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        hide();
    }

    @Subscribe
    private void handleTaskListEvent(ShowTaskListEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        show();
    }

}
