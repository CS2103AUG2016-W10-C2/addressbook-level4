package seedu.todolist.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import seedu.todolist.model.task.Entry;

// @@author A0116603R
/**
 * Represents a list of tasks to be displayed
 */
public class TaskList extends ListView<Entry> {
    private static String FXML = "TaskList.fxml";

    private ObservableList<Entry> data;

    public TaskList() {
        super();
        FXMLLoader loader = UiPartLoader.getLoader(FXML);
        loader.setRoot(this);
        loader.setController(this);
        UiPartLoader.loadNode(loader, FXML);
    }

    public void init(ObservableList<Entry> tasks) {
        data = tasks;
        setItems(data);
        setCellFactory(TaskViewCell.getFactory());
    }
}
