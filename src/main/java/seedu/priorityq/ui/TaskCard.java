package seedu.priorityq.ui;


import javafx.beans.value.ChangeListener;
import javafx.css.Styleable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import seedu.priorityq.model.task.Task;
import seedu.priorityq.model.task.Entry;
import seedu.priorityq.model.task.Event;

import static seedu.priorityq.ui.util.GuiUtil.*;

//@@author A0116603R
/**
 * Represents Tasks and Events in the TaskList
 */
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

    /**
     * Initialises UI elements which are common to floating tasks, deadlines, and
     * events. This includes the title, id, tags and description.
     */
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

    /**
     * Initialises UI elements which are task-specific. This refers to the checkbox and
     * the deadline.
     */
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
        checkBox.setSelected(task.isMarked());
        checkBox.selectedProperty().addListener(listener);
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

    /**
     * Initialises UI elements which are event-specific. This refers to the start time,
     * end time and date separator.
     */
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
