package seedu.address.logic.commands;

import seedu.address.logic.commands.UndoableCommand.CommandState;
import seedu.address.logic.commands.UndoableCommandHistory.UndoableCommandNotFoundException;
import seedu.address.model.Model;

/**
 * Redo the previous undo command.
 */
//@@author A0121501E
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_FAILURE = "No redoable commands found!";
    private UndoableCommandHistory undoableCommandHistory;

    public RedoCommand() {}
    
    public void setData(Model model, UndoableCommandHistory undoableCommandQueue) {
        this.model = model;
        this.undoableCommandHistory = undoableCommandQueue;
    }
    
    @Override
    public CommandResult execute() {
        try {
            UndoableCommand undoableCommand = undoableCommandHistory.getFromRedoStack();
            CommandResult undoableCommandResult = undoableCommand.reExecute();
            if (undoableCommand.getCommandState() == CommandState.UNDOABLE) {
                undoableCommandHistory.pushToUndoStack(undoableCommand);
            }
            return undoableCommandResult;
        } catch (UndoableCommandNotFoundException e) {
            return new CommandResult(MESSAGE_FAILURE);
        }
    }
}
