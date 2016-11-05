package seedu.todolist.ui;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import seedu.todolist.commons.core.EventsCenter;
import seedu.todolist.commons.core.LogsCenter;
import seedu.todolist.commons.events.ui.WindowResizeEvent;
import seedu.todolist.logic.Logic;

import java.util.logging.Logger;

import static seedu.todolist.ui.util.GuiUtil.LARGE_DISPLAY_WIDTH;
import static seedu.todolist.ui.util.GuiUtil.LARGE_STYLE_CLASS;

//@@author A0116603R
/**
 * Interaction point between Main Controller and rest of the app view(s)
 */
public class AppViewController {
    private static final Logger logger = LogsCenter.getLogger(AppViewController.class);

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
        EventsCenter.getInstance().registerHandler(this);
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

    // #################
    // # EVENT HANDLER #
    // #################
    @Subscribe
    private void handleWindowResizeEvent(WindowResizeEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.getNewWidth().compareTo(LARGE_DISPLAY_WIDTH) <= 0) {
            removeLargeStyleClass();
        } else {
            addLargeStyleClass();
        }
    }

    // ##################
    // # HELPER METHODS #
    // ##################

    /**
     * Adds the LARGE_STYLE_CLASS to the root layout if the style class is not
     * already added
     */
    private void removeLargeStyleClass() {
        if (rootLayout.getStyleClass().contains(LARGE_STYLE_CLASS)) {
            Platform.runLater(() -> rootLayout.getStyleClass().remove(LARGE_STYLE_CLASS));

        }
    }

    /**
     * Removes the LARGE_STYLE_CLASS from the root layout if the style class is
     * present
     */
    private void addLargeStyleClass() {
        if (!rootLayout.getStyleClass().contains(LARGE_STYLE_CLASS)) {
            Platform.runLater(() -> rootLayout.getStyleClass().add(LARGE_STYLE_CLASS));
        }
    }
}
