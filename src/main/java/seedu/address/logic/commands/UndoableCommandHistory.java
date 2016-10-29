package seedu.address.logic.commands;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Stack of successfully executed undoable commands.
 */
//@@author A0121501E
public class UndoableCommandHistory {
    Deque<UndoableCommand> commandInternalUndoQueue = new ArrayDeque<UndoableCommand>();
    Deque<UndoableCommand> commandInternalRedoQueue = new ArrayDeque<UndoableCommand>();
    public static class UndoableCommandNotFoundException extends Exception {};
    
    /** Push a new Undoable command to history.
     *  Resets the redo queue since the redo commands are no longer in sync.
     */
    public void pushToHistory(UndoableCommand undoableCommand) {
        commandInternalRedoQueue = new ArrayDeque<UndoableCommand>();
        commandInternalUndoQueue.addFirst(undoableCommand);
    }

    public void pushToRedoStack(UndoableCommand undoableCommand) {
        commandInternalRedoQueue.addFirst(undoableCommand);
    }
    
    public void pushToUndoStack(UndoableCommand undoableCommand) {
        commandInternalUndoQueue.addFirst(undoableCommand);
    }
    
    public UndoableCommand getFromUndoStack() throws UndoableCommandNotFoundException {
        if (commandInternalUndoQueue.isEmpty()){
            throw new UndoableCommandNotFoundException();
        }
        return commandInternalUndoQueue.poll();
    }
    
    public UndoableCommand getFromRedoStack() throws UndoableCommandNotFoundException {
        if (commandInternalRedoQueue.isEmpty()){
            throw new UndoableCommandNotFoundException();
        }
        return commandInternalRedoQueue.poll();
    }
}
