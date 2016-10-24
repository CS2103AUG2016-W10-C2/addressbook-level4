package seedu.address.ui;

import javafx.scene.layout.Pane;
import seedu.address.logic.Logic;

/**
 * Interaction point between Main Controller and rest of the app view(s)
 */
//@@author A0116603R
public class AppViewController {

    private Logic logic;

    private Pane rootLayout;

    AppViewController(Pane root) {
        this.rootLayout = root;
    }


    // ################
    // # INITIALISERS #
    // ################
    void init() {
        initChildViews();
    }

    private void initChildViews() {
        assert rootLayout != null;
        HelpViewController hvc = new HelpViewController();
        hvc.init(rootLayout);
        TaskViewController tvc = new TaskViewController(logic);
        tvc.init(rootLayout);

    }

    // #######################
    // # GETTERS AND SETTERS #
    // #######################

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

}
