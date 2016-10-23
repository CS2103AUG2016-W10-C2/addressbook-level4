package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import seedu.address.logic.Logic;

/**
 * Interaction point between Main Controller and rest of the app view(s)
 */
//@@author A0116603R
public class AppViewController {

    private static AppViewController instance;

    private Logic logic;

    @FXML
    private BorderPane appView;

    private AppViewController() {}

    public static AppViewController getInstance() {
        if (instance == null) {
            instance = new AppViewController();
        }
        return instance;
    }

    // ################
    // # INITIALISERS #
    // ################
    void init(Pane root) {
        appView = new BorderPane();
        root.getChildren().add(appView);
        initChildViews();
    }

    private void initChildViews() {
        TaskViewController tvc = new TaskViewController(logic);
        tvc.init();
    }

    // #######################
    // # GETTERS AND SETTERS #
    // #######################

    BorderPane getAppView() {
        return appView;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

}
