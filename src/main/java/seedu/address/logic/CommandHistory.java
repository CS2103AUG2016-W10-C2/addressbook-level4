package seedu.address.logic;

import java.util.LinkedList;

/**
 * @@author A0127828W
 *
 * Maintains an internal data structure to keep track of input commands
 * Use a pointer to indicate which command should be returned when getter methods are called
 */
public interface CommandHistory {
    /**
     * Append a new command, represented by a string, to an internal storage
     * @param string
     */
    void appendCommand(String string);

    /**
     * Return the previous command in the command history
     * @return
     */
    String getPreviousCommand();

    /**
     * Return the next command in the command history
     * @return
     */
    String getNextCommand();

    /**
     * Reset the position of pointer to the latest command
     */
    void resetPosition();
}
