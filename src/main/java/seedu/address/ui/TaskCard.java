package seedu.address.ui;


import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.task.Task;
import seedu.address.model.task.Entry;
import seedu.address.model.task.Event;

import static seedu.address.ui.util.GuiUtil.*;

//@@author A0116603R
/**
 * Represents Tasks and Events in the TaskList
 */
public class TaskCard extends HBox {
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
        tags.setText(entry.tagsString());
        description.setText(entry.getDescription());
    }

    private void initTaskSpecificElements(Task task) {
        initCheckbox(task);

        if (task.getDeadline() == null) {
            deadline.setOpacity(TRANSPARENT);
            return;
        }

        deadline.setText(task.getDeadlineDisplay().toUpperCase());
        String additionalStyleClass = getDeadlineStyling(task.getDeadline());
        if (!additionalStyleClass.isEmpty()) {
            deadline.getStyleClass().add(additionalStyleClass);
        }
    }

    private void initCheckbox(Task task) {
        checkBox.selectedProperty().addListener(listener);
        checkBox.setSelected(task.isMarked());
    }

    private void hideEventSpecificElements() {
        setEmptyText(startTime, separator, endTime);
        hide(startTime, separator, endTime);
    }

    private void initEventSpecificElements(Event event) {
        startTime.setText(event.getStartTimeDisplay().toUpperCase());
        separator.setText(EVENT_DATE_SEPARATOR);
        endTime.setText(event.getEndTimeDisplay().toUpperCase());

        String additionalStyleClass = getEventStyling(event.getStartTime(), event.getEndTime());
        if (!additionalStyleClass.isEmpty()) {
            startTime.getStyleClass().add(additionalStyleClass);
            separator.getStyleClass().add(additionalStyleClass);
            endTime.getStyleClass().add(additionalStyleClass);
        }
    }

    private void hideTaskSpecificElements() {
        setEmptyText(deadline);
        hide(deadline, checkBox);
    }

    private void hide(Node... nodes) {
        for (Node node : nodes) {
            node.setOpacity(TRANSPARENT);
        }
    }

    private void setEmptyText(Label... labels) {
        for (Label label : labels) {
            label.setText("");
        }
    }
}
