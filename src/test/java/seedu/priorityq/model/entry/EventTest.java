package seedu.priorityq.model.entry;

import org.junit.Before;
import org.junit.Test;
import org.ocpsoft.prettytime.shade.net.fortuna.ical4j.model.DateTime;

import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.entry.Entry;
import seedu.priorityq.model.entry.Event;
import seedu.priorityq.model.entry.Task;
import seedu.priorityq.model.entry.Title;
import seedu.priorityq.model.tag.Tag;
import seedu.priorityq.model.tag.UniqueTagList;

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
    private LocalDateTime lastModifiedTime;
    private UniqueTagList tags;
    private String description;
    private boolean isMarked;
    private static final int DAY_IN_MILLIS = 1 * 24 * 60 * 60 * 1000;

    @Before
    public void setUp() {
        try {
            title = new Title("test");
            startTime = LocalDateTime.parse("2016-10-10T10:00:00");
            endTime = startTime.plusHours(2);
            tags = new UniqueTagList(new Tag("tag1"));
            isMarked = false;
            description = "Description";
            lastModifiedTime = LocalDateTime.parse("2016-10-10T10:00:00");
            testEvent = new Event(title, startTime, endTime, tags, isMarked, description, -1, lastModifiedTime);
        } catch (IllegalValueException ive) {
            ive.printStackTrace();
        }
    }
    //@@author A0126539Y
    @Test
    public void inheritanceTest() {
        assertTrue(testEvent instanceof Entry);
    }

    @Test
    public void entryConstructor() {
        assertTrue(testEvent.equals(new Event((Entry)testEvent)));

        Entry task = new Task(title, null, tags, false, description, lastModifiedTime);
        assertFalse(new Task(task).equals(testEvent));
    }

    @Test(expected = IllegalArgumentException.class)
    public void startDateAfterEnd() {
        new Event(title, endTime.plusMinutes(1), endTime, tags, isMarked, description, -1, lastModifiedTime);
    }

    @Test
    public void getAsTextEqualToString() {
        assertEquals(testEvent.getAsText(), testEvent.toString());
    }

    @Test
    public void hashCodeTests() {
        Event copy = new Event(testEvent);
        assertNotEquals(testEvent.hashCode(), copy.hashCode());

        // test consistency
        assertEquals(testEvent.hashCode(), testEvent.hashCode());
        assertEquals(testEvent.hashCode(), testEvent.hashCode());

        copy.setDescription("another description");
        assertNotEquals(testEvent.hashCode(), copy.hashCode());
    }

    @Test
    public void equals() throws Exception {
        // test null
        assertFalse(testEvent.equals(null));

        // test other instance
        assertFalse(testEvent.equals(new Object()));

        // test reflexivity
        assertTrue(testEvent.equals(testEvent));


        // test symmetricity
        Event copy = new Event(testEvent);
        assertTrue(testEvent.equals(copy));
        assertTrue(copy.equals(testEvent));

        // test transitivity
        Event copy2 = new Event(testEvent);
        assertTrue(copy.equals(copy2));
        assertTrue(testEvent.equals(copy2));

        copy.setTitle(new Title("some other title"));
        assertFalse(testEvent.equals(copy));
    }

    @Test
    public void testRecurrence() throws InterruptedException {
        Event recurrenceBase = new Event(title, startTime, endTime, tags, isMarked, description, DAY_IN_MILLIS, lastModifiedTime);
        Event copy;
        LocalDateTime start;
        LocalDateTime end;

        copy = new Event(recurrenceBase);
        start = LocalDateTime.now().minusDays(1).plusMinutes(1);
        end = start.plusMinutes(5);
        copy.setStartTime(start);
        copy.setEndTime(end);
        copy = new Event(copy);
        assertEquals(copy.getStartTime(), start.plusDays(1));
        assertEquals(copy.getEndTime(), end.plusDays(1));

        copy = new Event(recurrenceBase);
        start = LocalDateTime.now().minusDays(5).plusMinutes(1);
        end = start.plusMinutes(5);
        copy.setStartTime(start);
        copy.setEndTime(end);
        copy = new Event(copy);
        System.out.println(copy.getStartTime());
        System.out.println(start.plusDays(6));
        assertEquals(copy.getStartTime(), start.plusDays(5));
        assertEquals(copy.getEndTime(), end.plusDays(5));
    }

    @Test
    public void getTimeObjectProperty() {
        testEvent.startTimeObjectProperty().equals(testEvent.startTime);
        testEvent.endTimeObjectProperty().equals(testEvent.endTime);
        testEvent.recursionObjectProperty().equals(testEvent.recursion);
    }
    //@@author A0127828W

    @Test(expected = ClassCastException.class)
    public void createEventFromEntry() {
        Entry eventEntry = new Event(title, startTime, endTime, tags, isMarked, description, -1, lastModifiedTime);
        assertTrue(new Event(eventEntry) instanceof Event);

        Entry taskEntry = new Task(title, endTime, tags, isMarked, description, lastModifiedTime);
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
        Event otherEvent = new Event(title, startTime, endTime, tags, isMarked, description, -1, lastModifiedTime);
        assertTrue(testEvent.isSameStateAs(otherEvent));
    }

}
