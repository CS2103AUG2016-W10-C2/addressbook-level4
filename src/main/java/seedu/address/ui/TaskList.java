package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import seedu.address.model.task.Entry;

/**
 * Represents a list of tasks to be displayed
 */
// @@author A0116603R
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
