package seedu.address.model.task;

import org.junit.Before;
import org.junit.Test;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * Created by joeleba on 23/10/16.
 * @@author A0127828W
 */
public class EventTest {
    private Event testEvent;
    private Title title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UniqueTagList tags;
    private String description;
    private boolean isMarked;

    @Before
    public void setUp() {
        try {
            title = new Title("test");
            startTime = LocalDateTime.now();
            endTime = LocalDateTime.now().plusHours(2);
            tags = new UniqueTagList(new Tag("tag1"));
            isMarked = false;
            description = "Description";
            testEvent = new Event(title, startTime, endTime, tags, isMarked, description);
        } catch (IllegalValueException ive) {
            ive.printStackTrace();
        }
    }

    @Test
    public void createEvent() {
        assertTrue(testEvent instanceof Event);
    }

    @Test(expected = ClassCastException.class)
    public void createEventFromEntry() {
        Entry eventEntry = new Event(title, startTime, endTime, tags, isMarked, description);
        assertTrue(new Event(eventEntry) instanceof Event);

        Entry taskEntry = new Task(title, endTime, tags, isMarked, description);
        // Should throw exception
        new Event(taskEntry);
    }

    @Test
    public void getStartTime() {
        assertEquals(testEvent.getStartTime(), startTime);
    }

    @Test
    public void setStartTime() {
        LocalDateTime newStartTime = startTime.minusDays(1);
        testEvent.setStartTime(newStartTime);
        assertEquals(testEvent.getStartTime(), newStartTime);
        testEvent.setStartTime(startTime);
        assertEquals(testEvent.getStartTime(), startTime);
    }

    @Test
    public void getEndTime() {
        assertEquals(testEvent.getEndTime(), endTime);
    }

    @Test
    public void setEndTime() {
        LocalDateTime newEndTime = endTime.minusDays(1);
        testEvent.setEndTime(newEndTime);
        assertEquals(testEvent.getEndTime(), newEndTime);
        testEvent.setEndTime(endTime);
        assertEquals(testEvent.getEndTime(), endTime);
    }

    @Test
    public void isSameStateAs() {
        Event otherEvent = new Event(title, startTime, endTime, tags, isMarked, description);
        assertTrue(testEvent.isSameStateAs(otherEvent));
    }

}