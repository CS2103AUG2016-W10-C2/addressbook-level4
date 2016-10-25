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

import java.time.LocalDateTime;
import java.time.LocalTime;

import static seedu.address.ui.util.GuiUtil.TRANSPARENT;

/**
 * Represents Tasks and Events in the TaskList
 */
//@@author A0116603R
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
        title.setText(entry.getTitle().fullTitle);
        id.setText(Integer.toString(index));
        tags.setText(entry.tagsString());
        description.setText(entry.getDescription());
        if (entry instanceof Task) {
            Task task = (Task) entry;
            checkBox.selectedProperty().addListener(listener);
            checkBox.setSelected(entry.isMarked());
            setEmptyText(startTime, endTime);
            hide(startTime, endTime);

            if (task.getDeadline() == null) {
                deadline.setOpacity(TRANSPARENT);
                return;
            }

            deadline.setText(task.getDeadlineDisplay().toUpperCase());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);
            if (task.getDeadline().isBefore(now)) {
                deadline.getStyleClass().add("overdue");
            } else if (task.getDeadline().isAfter(now) && task.getDeadline().isBefore(midnight)) {
                deadline.getStyleClass().add("today");
            }
        }

        if (entry instanceof Event) {
            Event event = (Event)entry;
            startTime.setText(event.getStartTimeDisplay().toUpperCase());
            endTime.setText(" - " + event.getEndTimeDisplay().toUpperCase());
            setEmptyText(deadline);
            hide(deadline, checkBox);
        }

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
