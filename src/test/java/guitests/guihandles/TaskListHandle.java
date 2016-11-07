package guitests.guihandles;


import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seedu.priorityq.TestApp;
import seedu.priorityq.model.entry.Entry;
import seedu.priorityq.model.entry.Task;
import seedu.priorityq.testutil.TestUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static seedu.priorityq.ui.util.GuiUtil.OPAQUE;

/**
 * Provides a handle for the panel containing the task list.
 */
public class TaskListHandle extends GuiHandle {

    public static final int NOT_FOUND = -1;
    private static final String CARD_PANE_ID = "#cardPane";
    private static final String TASK_LIST_ID = "#taskListView";

    public TaskListHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public ListView<Entry> getListView() {
        return (ListView<Entry>) getNode(TASK_LIST_ID);
    }

    /**
     * Returns true if the list is showing the task details correctly and in correct order.
     * @param entries A list of task in the correct order.
     */
    public boolean isListMatching(Entry... entries) {
        return this.isListMatching(0, entries);
    }

    /**
     * Returns true if the task list is currently visible
     */
    public boolean isVisible() {
        return getNode(TASK_LIST_ID).getOpacity() == OPAQUE;
    }

    /**
     * Returns true if the {@code entries} appear as the sub list (in that order) at position {@code startPosition}.
     */
    public boolean containsInOrder(int startPosition, Entry... entries) {
        List<Entry> entryList = getListView().getItems();

        // Return false if the list in panel is too short to contain the given list
        if (startPosition + entries.length > entryList.size()){
            return false;
        }

        // Return false if any of the entries doesn't match
        for (int i = 0; i < entries.length; i++) {
            if (!entryList.get(startPosition + i).getTitle().fullTitle.equals(entries[i].getTitle().fullTitle)){
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if the list is showing the task details correctly and in correct order.
     * @param startPosition The starting position of the sub list.
     * @param entries A list of task in the correct order.
     */
    public boolean isListMatching(int startPosition, Entry... entries) throws IllegalArgumentException {
        if (entries.length + startPosition != getListView().getItems().size()) {
            throw new IllegalArgumentException("List size mismatched\n" +
                    "Got " + (entries.length) + " entries" +
                    "Expected " + (getListView().getItems().size() - 1) + " entries");
        }
        assertTrue(this.containsInOrder(startPosition, entries));
        for (int i = 0; i < entries.length; i++) {
            final int scrollTo = i + startPosition;
            guiRobot.interact(() -> getListView().scrollTo(scrollTo));
            guiRobot.sleep(400);
            if (!TestUtil.compareCardAndEntry(getTaskCardHandle(startPosition + i), entries[i])) {
                return false;
            }
        }
        return true;
    }


    public TaskCardHandle navigateToEntry(String title) {
        final Optional<Entry> entry = getListView().getItems().stream().filter(p -> p.getTitle().fullTitle.equals(title)).findAny();
        if (!entry.isPresent()) {
            throw new IllegalStateException("Name not found: " + title);
        }

        return navigateToEntry(entry.get());
    }

    /**
     * Navigates the listview to display and select the task.
     */
    public TaskCardHandle navigateToEntry(Entry entry) {
        int index = getTaskIndex(entry);

        guiRobot.interact(() -> {
            getListView().scrollTo(index);
            guiRobot.sleep(400);
            getListView().getSelectionModel().select(index);
        });
        return getTaskCardHandle(entry);
    }


    /**
     * Returns the position of the task given, {@code NOT_FOUND} if not found in the list.
     */
    public int getTaskIndex(Entry targetEntry) {
        List<Entry> entries = getListView().getItems();
        for (int i = 0; i < entries.size(); i++) {
            if(entries.get(i).getTitle().equals(targetEntry.getTitle())){
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Gets a task from the list by index
     */
    public Entry getEntry(int index) {
        return getListView().getItems().get(index);
    }

    public TaskCardHandle getTaskCardHandle(int index) {
        return getTaskCardHandle(new Task(getEntry(index)));
    }

    public TaskCardHandle getTaskCardHandle(Entry entry) {
        Set<Node> nodes = getAllCardNodes();
        Optional<Node> taskCardNode = nodes.stream()
                .filter(n -> new TaskCardHandle(guiRobot, primaryStage, n).isSameEntry(entry))
                .findFirst();
        if (taskCardNode.isPresent()) {
            return new TaskCardHandle(guiRobot, primaryStage, taskCardNode.get());
        } else {
            return null;
        }
    }

    protected Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    public int getNumberOfTasks() {
        return getListView().getItems().size();
    }
}
