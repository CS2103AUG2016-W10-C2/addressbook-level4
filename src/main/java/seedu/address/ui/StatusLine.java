package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * A status line which provides feedback to the user, e.g.
 * after a command is entered.
 */
public class StatusLine extends HBox {
    private static final String FXML = "StatusLine.fxml";

    @FXML
    private Text message;

    public StatusLine() {
        FXMLLoader loader = UiPartLoader.getLoader(FXML);
        loader.setRoot(this);
        loader.setController(this);
        UiPartLoader.loadNode(loader, FXML);
    }
}
