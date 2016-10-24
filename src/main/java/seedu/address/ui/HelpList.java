package seedu.address.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;

/**
 * Represents a list of help cards to be displayed
 */
// @@author A0116603R
public class HelpList extends ListView<HelpList.HelpItem> {
    private static final String FXML = "HelpList.fxml";

    private static final String ADD_HELP = "ADD";
    private static final String ADD_HELP_TEXT = "add <task_name> [st/<start> end/<end> | dl/<deadline>] [#<tag_name> ...] [desc/<description>]";
    private static final String EDIT_HELP = "EDIT";
    private static final String EDIT_HELP_TEXT = "edit <task_id> [title/new title] [st/<start> end/<end> | deadline/<deadline>] [#<tags>...] [desc/<description>]";
    private static final String ESCAPE_HELP = "CLOSE HELP";
    private static final String ESCAPE_HELP_TEXT = "<ESCAPE-KEY>";
    private static final String LIST_HELP = "LIST";
    private static final String LIST_HELP_TEXT = "list [[keywords] [[after/<date>] [before/<date>] | [on/<date>]][#<tag_name> ...] [recurrence=<recurrence_value>] [desc=<description_value>]]";
    private static final String TAG_HELP = "TAG";
    private static final String TAG_HELP_TEXT = "tag <task_id> #<tag_name> [#<tag_name> ...]";
    private static final String UNTAG_HELP = "UNTAG";
    private static final String UNTAG_HELP_TEXT = "untag <task_id> #<tag_name> [#<tag_name> ...]";
    private static final String DELETE_HELP = "DELETE";
    private static final String DELETE_HELP_TEXT = "delete <task_id>";
    private static final String MARK_HELP = "MARK";
    private static final String MARK_HELP_TEXT = "mark <task_id>";
    private static final String UNMARK_HELP = "UNMARK";
    private static final String UNMARK_HELP_TEXT = "unmark <task_id>";

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
        data.add(new HelpItem(ESCAPE_HELP, ESCAPE_HELP_TEXT));
        data.add(new HelpItem(ADD_HELP, ADD_HELP_TEXT));
        data.add(new HelpItem(EDIT_HELP, EDIT_HELP_TEXT));
        data.add(new HelpItem(LIST_HELP, LIST_HELP_TEXT));
        data.add(new HelpItem(TAG_HELP, TAG_HELP_TEXT));
        data.add(new HelpItem(UNTAG_HELP, UNTAG_HELP_TEXT));
        data.add(new HelpItem(DELETE_HELP, DELETE_HELP_TEXT));
        data.add(new HelpItem(MARK_HELP, MARK_HELP_TEXT));
        data.add(new HelpItem(UNMARK_HELP, UNMARK_HELP_TEXT));
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
