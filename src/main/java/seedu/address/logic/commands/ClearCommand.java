package seedu.address.logic.commands;

import seedu.address.model.ReadOnlyTaskManager;
import seedu.address.model.TaskManager;

/**
 * Clears the address book.
 */
public class ClearCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Todo list has been cleared!";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo clear Todo list!";
    
    private ReadOnlyTaskManager originalTaskManager;

    @Override
    public CommandResult execute() {
        assert model != null;
        originalTaskManager = new TaskManager(model.getTaskManager());
        model.resetData(TaskManager.getEmptyAddressBook());
        setUndoable();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    //@@author A0121501E
    @Override
    public CommandResult unexecute() {
        if (getCommandState() != CommandState.UNDOABLE){
            return new CommandResult(MESSAGE_UNDO_FAIL);
        }
        assert model != null;
        model.resetData(originalTaskManager);
        setRedoable();
        return new CommandResult(MESSAGE_UNDO_SUCCESS);
    }
}
