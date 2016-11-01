package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import seedu.address.model.task.Entry;

/**
 * Provides a handle to a task card in the task list panel.
 */
public class TaskCardHandle extends GuiHandle {
    private static final String TITLE_FIELD_ID = "#title";
    private static final String DESCRIPTION_FIELD_ID = "#description";
    private static final String TAGS_FIELD_ID = "#tags";
    private static final String DEADLINE_FIELD_ID = "#deadline";
    private static final String CHECKBOX_FIELD_ID = "#checkBox";

    private Node node;

    TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node){
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    private String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    private boolean getMarkedStatusFromCheckbox(String fieldId) {
        return getMarkedStatusFromCheckbox(fieldId, node);
    }

    public String getTitle() {
        return getTextFromLabel(TITLE_FIELD_ID);
    }

    public String getDescription() {
        return getTextFromLabel(DESCRIPTION_FIELD_ID);
    }

    public String getTags() {
        return getTextFromLabel(TAGS_FIELD_ID);
    }

    public String getDeadline() {
        return getTextFromLabel(DEADLINE_FIELD_ID);
    }

    public boolean getIsMarked() {
        return getMarkedStatusFromCheckbox(CHECKBOX_FIELD_ID);
    }

    private CheckBox getCheckBox() {
        return (CheckBox) guiRobot.from(node).lookup(CHECKBOX_FIELD_ID).tryQuery().get();
    }

    public void toggleCheckBox() {
        CheckBox checkBox = getCheckBox();
        guiRobot.clickOn(checkBox);
    }

    public boolean isSameEntry(Entry entry){
        return getTitle().equals(entry.getTitle().fullTitle);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getTitle().equals(handle.getTitle())
                    && getDescription().equals(handle.getDescription()); //TODO: compare the rest
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getTitle() + " " + getDescription();
    }
}
