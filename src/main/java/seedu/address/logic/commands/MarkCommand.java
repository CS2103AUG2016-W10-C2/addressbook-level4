package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.logic.commands.UndoableCommand.CommandState;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniqueTaskList.EntryNotFoundException;

//@@author A0121501E
public class MarkCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the entry as completed. "
            + "Identified by the index number used in the last entry listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Marked Entry: %1$s";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo mark Entry: %1$s";

    private final int targetIndex;
    private Entry entryToMark;
    private boolean originalIsMarked;

    public MarkCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        if (getCommandState()==CommandState.PRE_EXECUTION) {
            UnmodifiableObservableList<Entry> lastShownList = model.getFilteredPersonList();
    
            if (lastShownList.size() < targetIndex) {
                indicateAttemptToExecuteIncorrectCommand();
                return new CommandResult(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
            }
    
            entryToMark = lastShownList.get(targetIndex - 1);
            originalIsMarked = entryToMark.isMarked();
        }
        
        try {
            model.markTask(entryToMark);
        } catch (EntryNotFoundException pnfe) {
            assert false : "The target entry cannot be missing";
        }

        setUndoable();
        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToMark));
    }

    @Override
    public CommandResult unexecute() {
        if (getCommandState() != CommandState.UNDOABLE){
            return new CommandResult(MESSAGE_UNDO_FAIL);
        }
        assert model != null;
        assert entryToMark != null;

        try {
            if (originalIsMarked) {
                model.markTask(entryToMark);
            } else {
                model.unmarkTask(entryToMark);
            }
            setRedoable();
        } catch (EntryNotFoundException enfe) {
            assert false : "The target entry cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, entryToMark));
    }
}
