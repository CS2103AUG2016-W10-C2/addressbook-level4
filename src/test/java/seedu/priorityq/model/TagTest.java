package seedu.priorityq.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.priorityq.model.tag.Tag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void isValidTagName() {
        //Valid tag names
        assertTrue(Tag.isValidTagName("1a _"));
        assertTrue(Tag.isValidTagName("a"));
        
        //Invalid tag names
        assertFalse(Tag.isValidTagName("")); // cannot be empty
        assertFalse(Tag.isValidTagName(" cat")); // cannot start with space
        assertFalse(Tag.isValidTagName("cat ")); // cannot end with space
        assertFalse(Tag.isValidTagName("cat*")); // only alphanumeric, _ or space (last char)
        assertFalse(Tag.isValidTagName("*cat")); // only alphanumeric, _ or space (first char)
        assertFalse(Tag.isValidTagName("c*t")); // only alphanumeric, _ or space (non first/last char)
    }


}
