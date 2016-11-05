package seedu.priorityq.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

// @@author A0116603R
/**
 * Represents a help item which can be displayed.
 */
public class HelpCard extends HBox {
    private static final String FXML = "HelpCard.fxml";

    // ########
    // # FXML #
    // ########
    @FXML
    private Label commandLabel;

    @FXML
    private Label helpText;

    HelpCard(){
        super();
        FXMLLoader loader = UiPartLoader.getLoader(FXML);
        loader.setRoot(this);
        loader.setController(this);
        UiPartLoader.loadNode(loader, FXML);
    }

    public void init(HelpList.HelpItem helpItem) {
        commandLabel.setText(helpItem.getCommand());
        helpText.setText(helpItem.getExplanation());
    }
}
