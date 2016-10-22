package seedu.address.ui;

import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.task.Task;
import seedu.address.model.task.Entry;
import seedu.address.model.task.Event;

public class TaskCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label tags;
    @FXML
    private Label deadline;
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;
    @FXML
    private Label mark;
    @FXML
    private Label description;

    private Entry entry;
    private int displayedIndex;

    public TaskCard(){

    }

    public static TaskCard load(Entry entry, int displayedIndex){
        TaskCard card = new TaskCard();
        card.entry = entry;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        name.setText(entry.getTitle().fullTitle);
        id.setText(displayedIndex + ". ");
        tags.setText(entry.tagsString());
        description.setText(entry.getDescription());
        if (entry instanceof Task) {
            Task task = (Task)entry;
            if (task.getDeadline() != null) {
                deadline.setText("deadline: " + task.getDeadline().toString());
            }
            else {
                deadline.setText("");
            }
            startTime.setText("");
            endTime.setText("");
        }

        if (entry instanceof Event) {
            Event event = (Event)entry;
            startTime.setText("Start time: " + event.getStartTime().toString());
            endTime.setText("End time: " + event.getEndTime().toString());
            deadline.setText("");
        }
        mark.setText(entry.markString());
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
