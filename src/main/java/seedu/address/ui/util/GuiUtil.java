package seedu.address.ui.util;

import javafx.beans.value.ChangeListener;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.MarkTaskEvent;

import java.time.LocalDateTime;
import java.time.LocalTime;

//@@author A0116603R
/**
 * Helper class which returns event handlers for user-initiated events
 */
public class GuiUtil {

    public static double OPAQUE = 1.0;
    public static double TRANSPARENT = 0.0;

    public static double DEFAULT_FADE_DURATION = 400;

    public static final String EVENT_DATE_SEPARATOR = " - ";

    public static final String EVENT_DESCRIPTION_STYLE_CLASS = "event";

    private static String PAST_STYLE_CLASS = "past";
    private static String ACTIVE_STYLE_CLASS = "present";
    private static String OVERDUE_STYLE_CLASS = "overdue";

    public static ChangeListener<Boolean> getCheckBoxEventListener(int idx) {
        return (ov, old_val, new_val) -> EventsCenter.getInstance().post(new MarkTaskEvent(idx, new_val));
    }

    /**
     * Get the style class for floating task elements such as id, title and
     * description.
     * @param isMarked true, if the current task is marked as done
     * @return a string, the style class for the floating task
     */
    public static  String getTaskStyling(boolean isMarked) {
        return getDeadlineStyling(isMarked, null);
    }

    /**
     * Get the style class for deadlines. The style differs depending on whether the
     * task is marked as done. If it is not done, it differs depending on whether it
     * is overdue. If it is not overdue, it differs depending on whether the deadline
     * is due by the end of the current day.
     * @param isMarked true, if the current task is marked as done
     * @param deadline a datetime which will be compared to the current time to
     *                 determine the style class
     * @return a string, the style class for the deadline
     */
    public static String getDeadlineStyling(boolean isMarked, LocalDateTime deadline) {
        if (isMarked) {
            return PAST_STYLE_CLASS;
        }

        if (deadline == null) {
            return "";
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);

        if (deadline.isBefore(now)) {
            return OVERDUE_STYLE_CLASS;
        }

        if (deadline.isAfter(now) && deadline.isBefore(midnight)) {
            return ACTIVE_STYLE_CLASS;
        }
        return "";
    }

    /**
     * Get the style class for events. The style differs depending on whether the
     * event is over. If it is not over, but currently ongoing, a different style
     * class is also applied.
     * @param startTime a datetime, the start of the event
     * @param endTime a datetime, the end of the event
     * @return a string, the style class for the event
     */
    public static String getEventStyling(LocalDateTime startTime, LocalDateTime endTime) {
        assert (startTime != null && endTime != null);
        LocalDateTime now = LocalDateTime.now();
        if (endTime.isBefore(now)) {
            return PAST_STYLE_CLASS;
        } else if (now.isAfter(startTime) && now.isBefore(endTime)) {
            return ACTIVE_STYLE_CLASS;
        }
        return "";
    }
}
