package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Entry;

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
    private Label mark;

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
        if (entry instanceof Deadline) {
        	deadline.setText("deadline: " + ((Deadline)entry).getDeadline() );
        } else {
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
