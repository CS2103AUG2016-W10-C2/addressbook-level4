package seedu.address.logic.commands;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Stack of successfully executed undoable commands.
 */
//@@author A0121501E
public class UndoableCommandHistory {
    Deque<UndoableCommand> commandInternalQueue = new ArrayDeque<UndoableCommand>();
    public static class UndoableCommandNotFoundException extends Exception {};
    
    public void push(UndoableCommand undoableCommand) {
        commandInternalQueue.addFirst(undoableCommand);
    }
    
    public UndoableCommand getMostRecentUndoableCommand() throws UndoableCommandNotFoundException {
        if (commandInternalQueue.isEmpty()){
            throw new UndoableCommandNotFoundException();
        }
        return commandInternalQueue.poll();
    }
}
