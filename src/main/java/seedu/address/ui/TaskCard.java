package seedu.address.ui;


import javafx.beans.value.ChangeListener;
import javafx.css.Styleable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.spreadsheet.Grid;
import seedu.address.model.task.Task;
import seedu.address.model.task.Entry;
import seedu.address.model.task.Event;

import static seedu.address.ui.util.GuiUtil.*;

/**
 * Represents Tasks and Events in the TaskList
 */
//@@author A0116603R
public class TaskCard extends VBox {
    private static final String FXML = "TaskCard.fxml";

    // ########
    // # FXML #
    // ########
    @FXML
    private Label id;

    @FXML
    private Label title;

    @FXML
    private Label tags;

    @FXML
    private Label description;

    @FXML
    private Label deadline;

    @FXML
    private Label startTime;

    @FXML
    private Label separator;

    @FXML
    private Label endTime;

    @FXML
    private CheckBox checkBox;

    private ChangeListener<Boolean> listener;

    // ########
    // # DATA #
    // ########

    private Entry entry;
    private int index;

    public TaskCard(){
        super();
        FXMLLoader loader = UiPartLoader.getLoader(FXML);
        loader.setRoot(this);
        loader.setController(this);
        UiPartLoader.loadNode(loader, FXML);
    }

    public void init(Entry entry, int index, ChangeListener<Boolean> listener) {
        this.entry = entry;
        this.index = index;
        this.listener = listener;
        this.checkBox.selectedProperty().bindBidirectional(entry.isMarkedProperty());
        initData();
    }

    private void initData() {
        initCommonElements();
        if (entry instanceof Task) {
            Task task = (Task) entry;
            initTaskSpecificElements(task);
            hideEventSpecificElements();
        }

        if (entry instanceof Event) {
            Event event = (Event)entry;
            initEventSpecificElements(event);
            hideTaskSpecificElements();
        }
    }

    private void initCommonElements() {
        title.setText(entry.getTitle().fullTitle);
        id.setText(Integer.toString(index));
        if (entry.tagsString().isEmpty()) {
            hide(tags);
        } else {
            tags.setText(entry.tagsString());
        }
        if (entry.getDescription() == null || entry.getDescription().isEmpty()) {
            hide(description);
        } else {
            description.setText(entry.getDescription());
        }
    }

    private void initTaskSpecificElements(Task task) {
        initCheckbox(task);

        if (task.getDeadline() == null) {
            deadline.setOpacity(TRANSPARENT);
            styleTask(getTaskStyling(task.isMarked()));
            return;
        }

        deadline.setText(task.getDeadlineDisplay().toUpperCase());
        styleDeadline(getDeadlineStyling(task.isMarked(), task.getDeadline()));
    }

    private void initCheckbox(Task task) {
        checkBox.selectedProperty().addListener(listener);
        checkBox.setSelected(task.isMarked());
    }

    private void styleTask(String styleClass) {
        if (!styleClass.isEmpty()) {
            addStyleClass(styleClass, getTaskElements());
        }
    }

    private void addStyleClass(String styleClass, Styleable... nodes) {
        for (Styleable node : nodes) {
            node.getStyleClass().add(styleClass);
        }
    }

    private Styleable[] getTaskElements() {
        return new Styleable[]{id, title, description};
    }

    private void styleDeadline(String styleClass) {
        if (!styleClass.isEmpty()) {
            styleTask(styleClass);
            addStyleClass(styleClass, deadline);
        }
    }

    private void hideEventSpecificElements() {
        setEmptyText(startTime, separator, endTime);
        hide(startTime, separator, endTime);
    }

    private void initEventSpecificElements(Event event) {
        startTime.setText(event.getStartTimeDisplay().toUpperCase());
        separator.setText(EVENT_DATE_SEPARATOR);
        endTime.setText(event.getEndTimeDisplay().toUpperCase());

        styleEvent(getEventStyling(event.getStartTime(), event.getEndTime()));
    }

    private void styleEvent(String styleClass) {
        addStyleClass(EVENT_DESCRIPTION_STYLE_CLASS, description);
        if (!styleClass.isEmpty()) {
            addStyleClass(styleClass, startTime, separator, endTime, id, title, description);
        }
    }

    private void hideTaskSpecificElements() {
        setEmptyText(deadline);
        hide(deadline, checkBox);
    }

    private void setEmptyText(Label... labels) {
        for (Label label : labels) {
            label.setText("");
        }
    }

    private void hide(Node... nodes) {
        for (Node node : nodes) {
            node.managedProperty().bind(node.visibleProperty());
            node.setVisible(false);
        }
    }
}
