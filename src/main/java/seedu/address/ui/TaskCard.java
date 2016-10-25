package seedu.address.ui;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.task.Task;
import seedu.address.model.task.Entry;
import seedu.address.model.task.Event;

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

    public void init(Entry entry, int index, EventHandler<ActionEvent> handler) {
        this.entry = entry;
        this.index = index;
        this.checkBox.setOnAction(handler);
        initData();
    }

    public void initData() {
        title.setText(entry.getTitle().fullTitle);
        id.setText(Integer.toString(index));
        tags.setText(entry.tagsString());
        description.setText(entry.getDescription());
        if (entry instanceof Task) {
            Task task = (Task) entry;
            if (task.getDeadline() != null) {
                deadline.setText(task.getDeadlineDisplay().toUpperCase());
            }
            else {
                deadline.setText("");
            }
            startTime.setText("");
            endTime.setText("");
        }

        if (entry instanceof Event) {
            Event event = (Event)entry;
            startTime.setText(event.getStartTimeDisplay().toUpperCase());
            endTime.setText(" - " + event.getEndTimeDisplay().toUpperCase());
            deadline.setText("");
        }
        checkBox.setSelected(entry.isMarked());
    }
}
