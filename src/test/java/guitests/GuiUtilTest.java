package guitests;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static seedu.priorityq.ui.util.GuiUtil.*;

//@@author A0116603R
public class GuiUtilTest {

    private LocalDateTime now = LocalDateTime.now();
    private LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);

    @Test
    public void taskStyling() {
        assertEquals("Unmarked floating tasks have no styling", "", getTaskStyling(false));
        assertEquals("Marked floating tasks have ." + PAST_STYLE_CLASS + " style class", PAST_STYLE_CLASS, getTaskStyling(true));
    }

    @Test
    public void deadlineStyling_marked_havePastStyleRegardlessOfDeadline() {
        String styleClass = getDeadlineStyling(true, now.minusWeeks(1));
        assertEquals(PAST_STYLE_CLASS, styleClass);

        styleClass = getDeadlineStyling(true, now);
        assertEquals(PAST_STYLE_CLASS, styleClass);

        styleClass = getDeadlineStyling(true, now.plusWeeks(1));
        assertEquals(PAST_STYLE_CLASS, styleClass);
    }

    @Test
    public void deadlineStyling_unmarked() {
        assertEquals(OVERDUE_STYLE_CLASS, getDeadlineStyling(false, now.minusWeeks(1)));
        assertEquals(ACTIVE_STYLE_CLASS, getDeadlineStyling(false, midnight.minusMinutes(1)));
        assertEquals("", getDeadlineStyling(false, now.plusWeeks(1)));
    }

    @Test
    public void eventStyling_past() {
        assertEquals(PAST_STYLE_CLASS, getEventStyling(now.minusDays(5), now.minusDays(4)));
        assertEquals(ACTIVE_STYLE_CLASS, getEventStyling(now.minusSeconds(1), now.plusHours(1)));
        assertEquals("", getEventStyling(now.plusDays(1), now.plusDays(2)));
    }

}
