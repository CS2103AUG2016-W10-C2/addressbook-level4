package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import seedu.address.logic.Logic;

/**
 * Controller which initializes the default Task View -- this consists of
 * a TaskList and a CommandArea
 */
//@@author A0116603R
public class TaskViewController extends Controller {
    private Logic logic;

    TaskViewController(Logic logic) {
        this.logic = logic;
    }

    // ################
    // # INITIALISERS #
    // ################
    void init() {
        initAppView();
    }

    private void initAppView() {
        BorderPane appView = getAppView();
        TaskList taskList = new TaskList();
        taskList.init(getDefaultTasks());
        appView.setCenter(taskList);
        appView.setBottom(new CommandArea(logic));
    }

    // ###################
    // # PRIVATE GETTERS #
    // ###################

    private BorderPane getAppView() {
        return AppViewController.getInstance().getAppView();
    }

    private ObservableList getDefaultTasks() {
        assert logic != null;
        return logic.getFilteredPersonList();
    }
}
