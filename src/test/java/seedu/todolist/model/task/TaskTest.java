package seedu.todolist.model.task;

import org.junit.Before;
import org.junit.Test;
import seedu.todolist.model.tag.Tag;
import seedu.todolist.model.tag.UniqueTagList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @@author A0127828W
 */
public class TaskTest {
    private Task floating;
    private Task withDeadline;
    private LocalDateTime deadline;
    private Title title;
    private UniqueTagList uniqueTagList;
    private String description;
    private LocalDateTime lastModifiedTime;

    @Before
    public void setUp() throws Exception {
        title = new Title("Test title");
        deadline = LocalDateTime.parse("2016-10-10T10:00");
        uniqueTagList = new UniqueTagList(new Tag("tag1"));
        boolean isMarked = false;
        description = "Something described";
        lastModifiedTime = LocalDateTime.parse("2016-10-10T12:00");

        floating = new Task(title, null, uniqueTagList, isMarked, description, lastModifiedTime);
        withDeadline = new Task(title, deadline, uniqueTagList, isMarked, description, lastModifiedTime);
    }

    @Test
    public void getDeadline_Present_Success() {
        assertEquals(deadline, withDeadline.getDeadline());
    }

    @Test
    public void getDeadline_FloatingTask_null() {
        assertEquals(null, floating.getDeadline());
    }

    @Test
    public void getDeadlineDisplay_floating_emptyString() {
        assertEquals("", floating.getDeadlineDisplay());
    }

    @Test
    public void getDeadlineDisplay_deadline_success() {
        Date interpreted = Date.from(deadline.atZone(ZoneId.systemDefault()).toInstant());
        String expected = Task.prettyTime.format(interpreted);

        assertEquals(expected, withDeadline.getDeadlineDisplay());
    }

    @Test
    public void setDeadline_nullAndNotNull_success() {
        withDeadline.setDeadline(null);
        assertEquals(null, withDeadline.getDeadline());
        withDeadline.setDeadline(deadline);
        assertEquals(deadline, withDeadline.getDeadline());
    }

    @Test
    public void equals() throws Exception {
        Task alt = new Task(title, deadline, uniqueTagList, false, description, lastModifiedTime);
        assertEquals(true, withDeadline.equals(alt));

        alt.setTitle(new Title("some other title"));
        assertEquals(false, withDeadline.equals(alt));
    }

    @Test
    public void isSameStateAs() throws Exception {
        Task alt = new Task(title, deadline, uniqueTagList, false, description, lastModifiedTime);
        assertTrue(withDeadline.isSameStateAs(alt));

        alt.setTitle(new Title("some other title"));
        assertFalse(withDeadline.isSameStateAs(alt));
    }

    @Test
    public void isFloatingTask() {
        assertTrue(floating.isFloatingTask());
        assertFalse(withDeadline.isFloatingTask());
    }

    @Test
    public void getAsText_floating() {
        String expectedFloating = "Test title (Something described) #TAG1";

        assertEquals(expectedFloating, floating.getAsText());
    }

    @Test
    public void getAsText_withDeadline() {
        String exprectedWithDeadline = "Test title (Something described) #TAG1 Due: " + withDeadline.getDeadlineDisplay();

        assertEquals(exprectedWithDeadline, withDeadline.getAsText());
    }

    @Test
    public void getDateDisplay_deadline_success() {
        Date interpreted = Date.from(deadline.atZone(ZoneId.systemDefault()).toInstant());
        String expected = Task.prettyTime.format(interpreted);

        assertEquals(expected, withDeadline.getDateDisplay(deadline));
    }

    @Test
    public void getDateDisplay_null_emptyString() {
        assertEquals("", withDeadline.getDateDisplay(null));
    }

    @Test
    public void getComparableTime_floating() {
        assertEquals(lastModifiedTime, floating.getComparableTime());
    }

    @Test
    public void getComparableTime_withDeadline() {
        assertEquals(deadline, withDeadline.getComparableTime());
    }

}