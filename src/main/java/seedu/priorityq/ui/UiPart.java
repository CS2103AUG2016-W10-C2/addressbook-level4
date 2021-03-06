package seedu.priorityq.ui;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import seedu.priorityq.commons.core.EventsCenter;
import seedu.priorityq.commons.events.BaseEvent;

/**
 * Base class for UI parts.
 * A 'UI part' represents a distinct part of the UI. e.g. Windows, dialogs, panels, status bars, etc.
 */
public abstract class UiPart {

    /**
     * The primary stage for the UI Part.
     */
    Stage primaryStage;

    public UiPart(){

    }

    /**
     * Raises the event via {@link EventsCenter#post(BaseEvent)}
     * @param event
     */
    protected void raise(BaseEvent event){
        EventsCenter.getInstance().post(event);
    }

    /**
     * Override this method to receive the main Node generated while loading the view from the .fxml file.
     * @param node
     */
    public abstract void setNode(Node node);

    /**
     * Override this method to return the name of the fxml file. e.g. {@code "MainController.fxml"}
     * @return
     */
    public abstract String getFxmlPath();

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Sets the placeholder for UI parts that reside inside another UI part.
     * @param placeholder
     */
    public void setPlaceholder(Pane placeholder) {
        //Do nothing by default.
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
