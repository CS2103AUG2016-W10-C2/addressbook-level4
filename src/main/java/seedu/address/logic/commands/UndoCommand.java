package seedu.address.logic.commands;

import seedu.address.logic.commands.UndoableCommandHistory.UndoableCommandNotFoundException;
import seedu.address.model.Model;

/**
 * Undo the previous undoable command.
 */
//@@author A0121501E
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_FAILURE = "No undoable commands found!";
    private UndoableCommandHistory undoableCommandHistory;

    public UndoCommand() {}
    
    public void setData(Model model, UndoableCommandHistory undoableCommandQueue) {
        this.model = model;
        this.undoableCommandHistory = undoableCommandQueue;
    }
    
    @Override
    public CommandResult execute() {
        try {
            UndoableCommand undoableCommand = undoableCommandHistory.getMostRecentUndoableCommand();
            return undoableCommand.unexecute();
        } catch (UndoableCommandNotFoundException e) {
            return new CommandResult(MESSAGE_FAILURE);
        }
    }
}
