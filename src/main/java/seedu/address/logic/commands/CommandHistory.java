package seedu.address.logic.commands;

import java.util.ArrayList;

/**
 * @@author A0127828W
 *
 * Maintains an internal data structure to keep track of input commands
 * Use a pointer to indicate which command should be returned when getter methods are called
 *
 * commandHistory: the internal structure to store commands
 * commandHistoryIndex: the index of last retrieved command.
 *      equals to 0 when the last retrieved command was the oldest
 *      equals to commandHistory.size() - 1 when the last retrieved command was the latest
 */
public class CommandHistory {
    private ArrayList<String> commandHistory;
    private Integer commandHistoryIndex;

    /**
     * Initialize command history
     * A commandHistoryIndex of -1 indicates that it has reached the limit
     */
    public CommandHistory() {
        commandHistory = new ArrayList<>();
        commandHistoryIndex = -1;
    }

    /**
     * Append a new command, represented by a string, to an internal storage
     * @param string
     */
    public void appendCommand(String string) {
        if (string == null || string.isEmpty()) {
            return;
        }
        commandHistory.add(string);
        commandHistoryIndex++;
    }

    /**
     * Return the previous command in the command history
     * @return
     */
    public String getPreviousCommand() {
        if (isAtOldestCommand()) {
            return null;
        }
        commandHistoryIndex--;
        String returnCommand = commandHistory.get(commandHistoryIndex);

        return returnCommand;
    }

    /**
     * Return the next command in the command history
     * @return
     */
    public String getNextCommand() {
        if (isAtNewestCommand()) {
            return "";
        }
        commandHistoryIndex++;
        String returnCommand = commandHistory.get(commandHistoryIndex);

        return returnCommand;
    }

    /**
     * Reset the position of pointer to the latest command
     */
    public void resetPosition() {
        commandHistoryIndex = commandHistory.size();
    }

    private boolean isAtOldestCommand() {
        return commandHistory.isEmpty()
            || commandHistoryIndex <= 0;
    }

    private boolean isAtNewestCommand() {
        return commandHistory.isEmpty()
            || commandHistoryIndex >= commandHistory.size() - 1;
    }
}
