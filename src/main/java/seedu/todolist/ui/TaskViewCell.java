package seedu.todolist.ui;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import seedu.todolist.model.task.Entry;
import seedu.todolist.ui.util.GuiUtil;

// @@author A0116603R
/**
 * Custom ListCell which is displayed using a TaskCard
 */
public class TaskViewCell extends ListCell<Entry> implements EntryViewCell {

    static <T> Callback<ListView<T>, ListCell<Entry>> getFactory() {
        return param -> new TaskViewCell();
    }

    @Override
    public void updateItem(Entry entry, boolean empty) {
        super.updateItem(entry, empty);
        if(empty || entry == null) {
            setGraphic(null);
            setText(null);
        } else {
            TaskCard taskCard = new TaskCard();
            int currIdx = getIndex() + 1;
            taskCard.init(entry, currIdx, GuiUtil.getCheckBoxEventListener(currIdx));
            setGraphic(taskCard);
        }

    }

}
