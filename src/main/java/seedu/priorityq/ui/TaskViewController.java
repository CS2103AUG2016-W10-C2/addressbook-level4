package seedu.priorityq.ui;

import com.google.common.eventbus.Subscribe;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import seedu.priorityq.commons.core.EventsCenter;
import seedu.priorityq.commons.core.LogsCenter;
import seedu.priorityq.commons.events.ui.FocusCommandLineEvent;
import seedu.priorityq.commons.events.ui.ShowHelpListEvent;
import seedu.priorityq.commons.events.ui.ShowTaskListEvent;
import seedu.priorityq.logic.Logic;
import seedu.priorityq.model.task.Entry;

import java.util.logging.Logger;

//@@author A0116603R
/**
 * Controller which initializes the default Task View -- this consists of
 * a TaskList and a CommandArea
 */
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
        addEnterHandlerForScene();
    }

    // ###################
    // # PRIVATE GETTERS #
    // ###################

    private ObservableList<Entry> getDefaultTasks() {
        assert logic != null;
        return logic.getFilteredEntryList();
    }

    /**
     * Register an event handler to enable focusing on the command
     * line if the user presses the <ENTER> key.
     */
    private void addEnterHandlerForScene() {
        assert appView != null;
        appView.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    EventsCenter.getInstance().post(new FocusCommandLineEvent());
                }
            }
        });
    }

    // ##################
    // # EVENT HANDLERS #
    // ##################
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
