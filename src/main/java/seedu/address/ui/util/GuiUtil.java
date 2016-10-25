package seedu.address.ui.util;

import javafx.beans.value.ChangeListener;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.MarkTaskEvent;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Helper class which returns event handlers for user-initiated events
 */
//@@author A0116603R
public class GuiUtil {

    public static double OPAQUE = 1.0;
    public static double TRANSPARENT = 0.0;

    public static double DEFAULT_FADE_DURATION = 400;

    public static final String EVENT_DATE_SEPARATOR = " - ";

    private static String PAST_STYLE_CLASS = "past";
    private static String ACTIVE_STYLE_CLASS = "present";

    public static ChangeListener<Boolean> getCheckBoxEventListener(int idx) {
        return (ov, old_val, new_val) -> EventsCenter.getInstance().post(new MarkTaskEvent(idx, new_val));
    }

    public static String getDeadlineStyling(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);
        if (dateTime.isBefore(now)) {
            return PAST_STYLE_CLASS;
        } else if (dateTime.isAfter(now) && dateTime.isBefore(midnight)) {
            return ACTIVE_STYLE_CLASS;
        }
        return "";
    }

    public static String getEventStyling(LocalDateTime startTime, LocalDateTime endTime) {
        LocalDateTime now = LocalDateTime.now();
        if (endTime.isBefore(now)) {
            return PAST_STYLE_CLASS;
        } else if (now.isAfter(startTime) && now.isBefore(endTime)) {
            return ACTIVE_STYLE_CLASS;
        }
        return "";
    }
}
