package seedu.todolist.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;

// @@author A0116603R
/**
 * Represents a list of help cards to be displayed
 */
public class HelpList extends ListView<HelpList.HelpItem> {
    private static final String FXML = "HelpList.fxml";

    private static final String[] COMMANDS = new String[]{
            "CLOSE HELP",
            "ADD", "EDIT",
            "DELETE", "LIST",
            "LIST-ALL",
            "TAG", "UNTAG",
            "MARK", "UNMARK",
            "UNDO", "OPTION"};

    private static final String[] HELP_TEXT = new String[]{
            "<ESCAPE-KEY>",
            "add <task_name> [start/<start> end/<end>] [#<tag_name> ...] [r/<recurrence>] [desc/<description>]",
            "edit <task_id> [title/new title] [start/<start> end/<end>] [#<tags>...] [r/ <recurrence>] [desc/<description>]",
            "delete <task_id>",
            "list [[keywords] [[after/<date>] [before/<date>] | [on/<date>]][#<tag_name> ...] [desc=<description_value>] [type/{task, event}]]",
            "list-all [[keywords] [[after/<date>] [before/<date>] | [on/<date>]][#<tag_name> ...] [desc=<description_value>]]",
            "tag <task_id> #<tag_name> [#<tag_name> ...]",
            "untag <task_id> #<tag_name> [#<tag_name> ...]",
            "mark <task_id>",
            "unmark <task_id>",
            "undo",
            "option [<type>/<value> ...]"};

    private ObservableList<HelpItem> data;

    HelpList() {
        super();
        FXMLLoader loader = UiPartLoader.getLoader(FXML);
        loader.setRoot(this);
        loader.setController(this);
        UiPartLoader.loadNode(loader, FXML);
    }

    public void init() {
        initHelpItems();
        setItems(data);
        setCellFactory(HelpViewCell.getFactory());
    }

    private void initHelpItems() {
        data = FXCollections.observableArrayList();

        assert (COMMANDS.length == HELP_TEXT.length);
        for (int i=0; i<COMMANDS.length; i++) {
            data.add(new HelpItem(COMMANDS[i], HELP_TEXT[i]));
        }
    }

    class HelpItem {
        private String command;
        private String explanation;

        HelpItem(String command, String helpText) {
            this.command = command;
            explanation = helpText;
        }

        public String getCommand() {
            return command;
        }

        String getExplanation() {
            return explanation;
        }
    }
}
