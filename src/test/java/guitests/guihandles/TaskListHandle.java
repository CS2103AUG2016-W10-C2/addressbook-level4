package guitests.guihandles;


import guitests.GuiRobot;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import seedu.address.TestApp;
import seedu.address.model.task.Task;
import seedu.address.model.task.Entry;
import seedu.address.testutil.TestUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Provides a handle for the panel containing the task list.
 */
public class TaskListHandle extends GuiHandle {

    public static final int NOT_FOUND = -1;
    public static final String CARD_PANE_ID = "#cardPane";

    private static final String TASK_LIST_ID = "#taskListView";

    public TaskListHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public List<Entry> getSelectedPersons() {
        ListView<Entry> personList = getListView();
        return personList.getSelectionModel().getSelectedItems();
    }

    public ListView<Entry> getListView() {
        return (ListView<Entry>) getNode(TASK_LIST_ID);
    }

    /**
     * Returns true if the list is showing the task details correctly and in correct order.
     * @param persons A list of task in the correct order.
     */
    public boolean isListMatching(Entry... persons) {
        return this.isListMatching(0, persons);
    }
    
    /**
     * Clicks on the ListView.
     */
    public void clickOnListView() {
        Point2D point= TestUtil.getScreenMidPoint(getListView());
        guiRobot.clickOn(point.getX(), point.getY());
    }

    /**
     * Returns true if the {@code persons} appear as the sub list (in that order) at position {@code startPosition}.
     */
    public boolean containsInOrder(int startPosition, Entry... entries) {
        List<Entry> personsInList = getListView().getItems();

        // Return false if the list in panel is too short to contain the given list
        if (startPosition + entries.length > personsInList.size()){
            return false;
        }

        // Return false if any of the persons doesn't match
        for (int i = 0; i < entries.length; i++) {
            if (!personsInList.get(startPosition + i).getTitle().fullTitle.equals(entries[i].getTitle().fullTitle)){
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if the list is showing the task details correctly and in correct order.
     * @param startPosition The starting position of the sub list.
     * @param persons A list of task in the correct order.
     */
    public boolean isListMatching(int startPosition, Entry... persons) throws IllegalArgumentException {
        if (persons.length + startPosition != getListView().getItems().size()) {
            throw new IllegalArgumentException("List size mismatched\n" +
                    "Expected " + (getListView().getItems().size() - 1) + " persons");
        }
        assertTrue(this.containsInOrder(startPosition, persons));
        for (int i = 0; i < persons.length; i++) {
            final int scrollTo = i + startPosition;
            guiRobot.interact(() -> getListView().scrollTo(scrollTo));
            guiRobot.sleep(200);
            if (!TestUtil.compareCardAndEntry(getPersonCardHandle(startPosition + i), persons[i])) {
                return false;
            }
        }
        return true;
    }


    public TaskCardHandle navigateToPerson(String title) {
        guiRobot.sleep(500); //Allow a bit of time for the list to be updated
        final Optional<Entry> entry = getListView().getItems().stream().filter(p -> p.getTitle().fullTitle.equals(title)).findAny();
        if (!entry.isPresent()) {
            throw new IllegalStateException("Name not found: " + title);
        }

        return navigateToPerson(entry.get());
    }

    /**
     * Navigates the listview to display and select the task.
     */
    public TaskCardHandle navigateToPerson(Entry person) {
        int index = getPersonIndex(person);

        guiRobot.interact(() -> {
            getListView().scrollTo(index);
            guiRobot.sleep(150);
            getListView().getSelectionModel().select(index);
        });
        guiRobot.sleep(100);
        return getPersonCardHandle(person);
    }


    /**
     * Returns the position of the task given, {@code NOT_FOUND} if not found in the list.
     */
    public int getPersonIndex(Entry targetEntry) {
        List<Entry> personsInList = getListView().getItems();
        for (int i = 0; i < personsInList.size(); i++) {
            if(personsInList.get(i).getTitle().equals(targetEntry.getTitle())){
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Gets a task from the list by index
     */
    public Entry getPerson(int index) {
        return getListView().getItems().get(index);
    }

    public TaskCardHandle getPersonCardHandle(int index) {
        return getPersonCardHandle(new Task(getListView().getItems().get(index)));
    }

    public TaskCardHandle getPersonCardHandle(Entry person) {
        Set<Node> nodes = getAllCardNodes();
        Optional<Node> personCardNode = nodes.stream()
                .filter(n -> new TaskCardHandle(guiRobot, primaryStage, n).isSameEntry(person))
                .findFirst();
        if (personCardNode.isPresent()) {
            return new TaskCardHandle(guiRobot, primaryStage, personCardNode.get());
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