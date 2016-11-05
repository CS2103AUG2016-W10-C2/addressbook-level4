package seedu.priorityq.logic.commands;

//@@author A0121501E
/**
 * Represents a command which changes to the model can be undone using the undo command
 * and redone using the redo command
 */
public abstract class UndoableCommand extends Command {

    public enum CommandState {UNDOABLE, REDOABLE, PRE_EXECUTION};
    
    public String MESSAGE_UNDO_FAIL = "Command cannot be undone before it is successfully executed!";
    public String MESSAGE_REDO_FAIL = "Command cannot be redone before it is successfully unexecuted!";
    public CommandState commandState = CommandState.PRE_EXECUTION;
    public abstract CommandResult unexecute();

    /**
     * Re-executes the command which was previously undone.
     */
    public CommandResult reExecute() {
        if (getCommandState()!=CommandState.REDOABLE){
            return new CommandResult(MESSAGE_UNDO_FAIL);
        }
        return execute();
    }
    
    public void setUndoable() {
        commandState = CommandState.UNDOABLE;
    }
    
    public void setRedoable() {
        commandState = CommandState.REDOABLE;
    }
    
    public CommandState getCommandState() {
        return commandState;
    }
}
