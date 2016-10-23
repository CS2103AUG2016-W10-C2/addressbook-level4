package seedu.address.ui;

import javafx.scene.layout.Pane;
import seedu.address.logic.Logic;

/**
 * Interaction point between Main Controller and rest of the app view(s)
 */
//@@author A0116603R
public class AppViewController {

    private static AppViewController instance;

    private Logic logic;

    private Pane rootLayout;

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
        if (rootLayout == null) {
            this.rootLayout = root;
        }
        initChildViews();
    }

    private void initChildViews() {
        assert rootLayout != null;
        HelpViewController hvc = new HelpViewController();
        hvc.init();
        TaskViewController tvc = new TaskViewController(logic);
        tvc.init();

    }

    // #######################
    // # GETTERS AND SETTERS #
    // #######################

    Pane getRootLayout() {
        return rootLayout;
    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

}
