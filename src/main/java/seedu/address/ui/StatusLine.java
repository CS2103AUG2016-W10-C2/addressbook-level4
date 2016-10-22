package seedu.address.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StatusLine extends UiPart implements StatusDisplay {
    private static final String FXML = "StatusLine.fxml";

    private HBox node;
    private Pane placeHolder;
    private Text statusLineDisplay;

    @FXML
    private Text message = new Text();

    public static StatusLine load(Stage primaryStage, Pane statusLineView) {
        StatusLine statusLine = UiPartLoader.loadUiPart(primaryStage, statusLineView, new StatusLine());
        statusLine.configure();
        return statusLine;
    }

    public void configure() {
        placeHolder.getChildren().add(message);
    }

    @Override
    public void setNode(Node node) {
        this.node = (HBox) node;
    }

    @Override
    public void setPlaceholder(Pane placeholder) {
        this.placeHolder = placeholder;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    public void postMessage(String feedbackToUser) {
        message.setText(feedbackToUser);
    }
}
