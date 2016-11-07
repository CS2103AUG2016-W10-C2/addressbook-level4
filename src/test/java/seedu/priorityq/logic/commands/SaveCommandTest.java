package seedu.priorityq.logic.commands;

import static org.junit.Assert.fail;

import java.nio.file.InvalidPathException;

import org.junit.Test;

import seedu.priorityq.commons.exceptions.IllegalValueException;


//@@author A0126539Y
public class SaveCommandTest {

    @Test(expected = IllegalValueException.class)
    public void nullLocation() throws Exception {
        new SaveCommand(null);
    }
    
    @Test(expected = IllegalValueException.class)
    public void emptyStringLocation() throws Exception {
        new SaveCommand("");
    }
    
    @Test(expected = InvalidPathException.class)
    public void invalidLocation() throws Exception{
        new SaveCommand("invalid.json");
    }
    
    @Test
    public void saveCommand() {
        try {
            new SaveCommand("data/newLocation.xml");
        } catch (Exception e) {
            fail();
        }
    }
}
