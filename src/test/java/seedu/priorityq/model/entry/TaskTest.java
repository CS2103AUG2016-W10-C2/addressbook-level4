package seedu.priorityq.model.entry;

import org.junit.Before;
import org.junit.Test;
import seedu.priorityq.model.entry.Task;
import seedu.priorityq.model.entry.Title;
import seedu.priorityq.model.tag.Tag;
import seedu.priorityq.model.tag.UniqueTagList;

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
    
    //@@author A0126539Y
    @Test
    public void inheritanceTest() {
        assertTrue(floating instanceof Entry);
    }
    
    @Test
    public void simpleConstructor() {
        new Task(title, uniqueTagList);
    }
    
    @Test
    public void constructor_nullDeadline() {
        new Task(title, null, uniqueTagList, false, description, lastModifiedTime);
    }
    
    @Test
    public void entryConstructor() {
        assertTrue(floating.equals(new Task((Entry)floating)));
        assertTrue(withDeadline.equals(new Task((Entry)withDeadline)));
        
        Entry event = new Event(title, LocalDateTime.MIN, LocalDateTime.MAX, uniqueTagList, false, description, 0, lastModifiedTime);
        assertFalse(new Task(event).equals(event));
    }
    
    @Test
    public void getAsTextEqualToString() {
        assertEquals(floating.getAsText(), floating.toString());
    }
    
    @Test
    public void hashCodeTests() {
        Task copy = new Task(floating);
        assertEquals(floating.hashCode(), copy.hashCode());
        Task deadlineCopy = new Task(withDeadline);
        assertEquals(withDeadline.hashCode(), deadlineCopy.hashCode());
        
        assertNotEquals(floating.hashCode(), withDeadline.hashCode());
        
        // test consistency
        assertEquals(floating.hashCode(), floating.hashCode());
        assertEquals(floating.hashCode(), floating.hashCode());
        
        copy.setDescription("new description");
        assertNotEquals(floating.hashCode(), copy.hashCode());
    }
    
    @Test
    public void equals() throws Exception {
        // test null
        assertFalse(floating.equals(null));
        assertFalse(withDeadline.equals(null));
        
        // test other instance
        assertFalse(floating.equals(new Object()));
        assertFalse(withDeadline.equals(new Object()));
        
        // test reflexivity
        assertTrue(withDeadline.equals(withDeadline));
        assertTrue(floating.equals(floating));
        
        
        // test symmetricity
        Task copy = new Task(floating);
        assertTrue(floating.equals(copy));
        assertTrue(copy.equals(floating));
        
        Task copyDeadline = new Task(withDeadline);
        assertTrue(withDeadline.equals(copyDeadline));
        assertTrue(copyDeadline.equals(withDeadline));
        
        // test transitivity
        Task copy2 = new Task(title, null, uniqueTagList, false, description, lastModifiedTime);
        assertTrue(copy.equals(copy2));
        assertTrue(floating.equals(copy2));
        

        copy.setTitle(new Title("some other title"));
        assertFalse(withDeadline.equals(copy));
        copy2.mark();
        assertFalse(withDeadline.equals(copy2));
    }
    //@@author

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
        String expected = withDeadline.getDateFormatter().format(interpreted);

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
        String expected = withDeadline.getDateFormatter().format(interpreted);
        
        System.out.println(expected);
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