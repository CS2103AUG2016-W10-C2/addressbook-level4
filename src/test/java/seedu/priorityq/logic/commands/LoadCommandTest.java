package seedu.priorityq.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.file.InvalidPathException;

import org.junit.Test;

import seedu.priorityq.commons.exceptions.IllegalValueException;


//@@author A0126539Y
public class LoadCommandTest {

    @Test(expected = IllegalValueException.class)
    public void nullLocation() throws Exception {
        new LoadCommand(null);
    }
    
    @Test(expected = IllegalValueException.class)
    public void emptyStringLocation() throws Exception {
        new LoadCommand("");
    }
    
    @Test(expected = InvalidPathException.class)
    public void invalidLocation() throws Exception{
        new LoadCommand("invalid.json");
    }
    
    @Test
    public void loadCommand() {
        try {
            new LoadCommand("data/PriorityQ.xml");
        } catch (Exception e) {
            fail();
        }
    }
}
