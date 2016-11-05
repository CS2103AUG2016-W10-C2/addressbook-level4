package seedu.todolist.ui;

import javafx.animation.FadeTransition;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import seedu.todolist.commons.core.EventsCenter;

import static seedu.todolist.ui.util.GuiUtil.DEFAULT_FADE_DURATION;
import static seedu.todolist.ui.util.GuiUtil.OPAQUE;
import static seedu.todolist.ui.util.GuiUtil.TRANSPARENT;

//@@author A0116603R
/**
 * Abstract class for all app view controllers.
 * Forces controllers to implement an init method.
 */
abstract class Controller {
    BorderPane appView;

    abstract void init(Pane root);

    abstract void initAppView();

    void addToRootView(Pane root) {
        root.getChildren().add(appView);
    }

    void registerAsEventHandler(Object handler) {
        EventsCenter.getInstance().registerHandler(handler);
    }


    void show() {
        assert appView != null;
        appView.setVisible(true);
        appView.toFront();
        getFadeTransition(DEFAULT_FADE_DURATION, OPAQUE).play();
    }

    void hide() {
        assert appView != null;
        getFadeTransition(DEFAULT_FADE_DURATION, TRANSPARENT).play();
        appView.toBack();
        appView.setVisible(false);
    }

    private FadeTransition getFadeTransition(double duration, double newValue) {
        FadeTransition transition = new FadeTransition(new Duration(duration), appView);
        transition.setToValue(newValue);
        return transition;
    }
}
