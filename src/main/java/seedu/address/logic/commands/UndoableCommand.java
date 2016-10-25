package seedu.address.logic.commands;

/**
 * Represents a command which changes to the model can be undone using the undo command
 */
//@@author A0121501E
public abstract class UndoableCommand extends Command {
    public String MESSAGE_UNDO_FAIL = "Command cannot be undone before it is successfully executed!";
    protected boolean executionIsSuccessful=false;

    public abstract CommandResult unexecute();

    public void setExecutionIsSuccessful() {
        executionIsSuccessful=true;
    }
    
    public boolean getExecutionIsSuccessful() {
        return executionIsSuccessful;
    }
}
