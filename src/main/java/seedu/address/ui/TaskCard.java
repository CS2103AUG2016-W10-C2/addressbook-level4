package seedu.address.ui;

import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import seedu.address.model.task.Task;
import seedu.address.model.task.Entry;

public class TaskCard extends UiPart{

    private static final String FXML = "TaskListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Text name;
    @FXML
    private Text id;
    @FXML
    private Text tags;
    @FXML
    private Text deadline;
    @FXML
    private Text mark;
    @FXML
    private Text description;

    private Entry entry;
    private int displayedIndex;

    public TaskCard(){

    }

    public static TaskCard load(Entry person, int displayedIndex){
        TaskCard card = new TaskCard();
        card.entry = person;
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
            Task task = (Task) entry;
            if (task.getDeadline() != null) {
                deadline.setText("due: " + task.getDeadline() );
            }
            else {
                deadline.setText("");
            }
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
