package seedu.priorityq.logic.commands;

import seedu.priorityq.logic.commands.UndoableCommand.CommandState;
import seedu.priorityq.logic.commands.UndoableCommandHistory.UndoableCommandNotFoundException;
import seedu.priorityq.model.Model;

//@@author A0121501E
/**
 * Undo the previous undoable command.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_FAILURE = "No undoable commands found!";
    private UndoableCommandHistory undoableCommandHistory;

    public void setData(Model model, UndoableCommandHistory undoableCommandQueue) {
        this.model = model;
        this.undoableCommandHistory = undoableCommandQueue;
    }
    
    @Override
    public CommandResult execute() {
        try {
            UndoableCommand undoableCommand = undoableCommandHistory.getFromUndoStack();
            CommandResult undoableCommandResult = undoableCommand.unexecute();
            if (undoableCommand.getCommandState() == CommandState.REDOABLE) {
                undoableCommandHistory.pushToRedoStack(undoableCommand);
            }
            return undoableCommandResult;
        } catch (UndoableCommandNotFoundException e) {
            return new CommandResult(MESSAGE_FAILURE);
        }
    }
}
