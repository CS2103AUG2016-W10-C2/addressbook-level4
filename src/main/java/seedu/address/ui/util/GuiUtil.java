package seedu.address.ui.util;

import javafx.beans.value.ChangeListener;
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

    public static ChangeListener<Boolean> getCheckBoxEventListener(int idx) {
        return (ov, old_val, new_val) -> EventsCenter.getInstance().post(new MarkTaskEvent(idx, new_val));
    }
}
