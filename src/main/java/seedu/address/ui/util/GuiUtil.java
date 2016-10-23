package seedu.address.ui.util;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.MarkTaskEvent;

/**
 * Helper class which returns event handlers for user-initiated events
 */
//@@author A0116603R
public class GuiUtil {

    public static double OPAQUE = 1.0;
    public static double TRANSPARENT = 0.0;

    public static double DEFAULT_FADE_DURATION = 400;

    public static EventHandler<ActionEvent> getCheckBoxEventHandler(int idx) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (event.getSource() instanceof CheckBox) {
                    CheckBox chkBox = (CheckBox) event.getSource();
                    EventsCenter.getInstance().post(new MarkTaskEvent(idx, chkBox.isSelected()));
                }
            }
        };
    }
}
