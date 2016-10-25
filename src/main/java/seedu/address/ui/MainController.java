package seedu.address.ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.ExitAppRequestEvent;
import seedu.address.commons.events.ui.ShowTaskListEvent;
import seedu.address.logic.Logic;
import seedu.address.model.UserPrefs;

import java.net.URL;
import java.util.logging.Logger;

/**
 * The Main Controller, which is in charge of the root layout and
 * enables other UI elements to be placed within the application.
 */
//@@author A0116603R
public class MainController extends UiPart {

    // #############
    // # CONSTANTS #
    // #############
    private static final String FXML = "RootLayout.fxml";
    private static final String CSS_SOURCE = "/view/PriorityQTheme.css";
    private static final int MIN_HEIGHT = 760;
    private static final int MIN_WIDTH = 520;

    private static final Logger logger = LogsCenter.getLogger(MainController.class);

    // ########
    // # FXML #
    // ########
    private Scene scene;

    @FXML
    private StackPane rootLayout;

    private Logic logic;

    public MainController() {
        super();
    }

    // ##################
    // # UIPART METHODS #
    // ##################
    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setNode(Node node) {
        rootLayout = (StackPane) node;
    }

    static MainController load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {
        MainController mainController = UiPartLoader.loadUiPart(primaryStage, new MainController());
        mainController.configure(config, prefs, logic);
        return mainController;
    }

    private void configure(Config config, UserPrefs prefs, Logic logic) {
        this.logic = logic;

        setTitle(config.getAppTitle());
        setWindowMinSize();
        setWindowDefaultSize(prefs);

        initScene();
        assert scene != null;
        primaryStage.setScene(scene);

        initAppController();
    }

    private void initScene() {
        assert rootLayout != null;
        logger.info("Initialising Scene...");
        scene = new Scene(rootLayout);

        configureScene();
    }

    private void configureScene() {
        assert scene != null;
        URL url = this.getClass().getResource(CSS_SOURCE);
        String css = url.toExternalForm();
        scene.getStylesheets().add(css);
        addEscapeHandlerForScene();
    }

    /**
     * Event filter to leave the help screen when the `ESCAPE` key is pressed.
     * A filter is used instead of a handler since the ListView or a particular
     * JavaFX node consumes the event only in the case of an `ESCAPE` key.
     */
    private void addEscapeHandlerForScene() {
        assert scene != null;
        scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    EventsCenter.getInstance().post(new ShowTaskListEvent());
                    ke.consume();
                }
            }
        });
    }

    private void initAppController() {
        AppViewController avc = new AppViewController(rootLayout);
        avc.setLogic(logic);
        avc.init();
    }

    // ##############
    // # UI CONFIG  #
    // ##############
    private void setTitle(String appTitle) {
        primaryStage.setTitle(appTitle);
    }

    private void setWindowDefaultSize(UserPrefs prefs) {
        primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
        primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
        if (prefs.getGuiSettings().getWindowCoordinates() != null) {
            primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
            primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
        }
    }

    private void setWindowMinSize() {
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.setMinWidth(MIN_WIDTH);
    }

    // #################
    // # FXML HANDLERS #
    // #################
    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        raise(new ExitAppRequestEvent());
    }

    // ###############
    // # GUI HELPERS #
    // ###############

    void show() {
        primaryStage.show();
    }

    void hide() {
        primaryStage.hide();
    }

    /**
     * Returns the current size and the position of the main Window.
     */
    GuiSettings getCurrentGuiSetting() {
        return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
    }

}
