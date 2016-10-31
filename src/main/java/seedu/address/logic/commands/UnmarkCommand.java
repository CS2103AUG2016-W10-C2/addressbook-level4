package seedu.address.logic.commands;

import java.time.LocalDateTime;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.logic.commands.UndoableCommand.CommandState;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniqueTaskList.EntryNotFoundException;

//@@author A0121501E
public class UnmarkCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the entry as completed. "
            + "Identified by the index number used in the last entry listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Unmarked Entry: %1$s";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo unmarked Entry: %1$s";

    private final int targetIndex;
    private Entry entryToUnmark;
    private boolean originalIsMarked;
    private LocalDateTime originalLastModifiedTime;

    public UnmarkCommand(int targetIndex) {
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
    
            entryToUnmark = lastShownList.get(targetIndex - 1);
            originalLastModifiedTime = entryToUnmark.getLastModifiedTime().plusDays(0);
            originalIsMarked= entryToUnmark.isMarked();
        }

        try {
            model.unmarkTask(entryToUnmark);
        } catch (EntryNotFoundException pnfe) {
            assert false : "The target entry cannot be missing";
        }
        setUndoable();
        return new CommandResult(String.format(MESSAGE_SUCCESS, entryToUnmark));
    }

    @Override
    public CommandResult unexecute() {
        if (getCommandState() != CommandState.UNDOABLE){
            return new CommandResult(MESSAGE_UNDO_FAIL);
        }
        assert model != null;
        assert entryToUnmark != null;

        try {
            if (originalIsMarked) {
                model.markTask(entryToUnmark);
            } else {
                model.unmarkTask(entryToUnmark);
            }
            model.updateLastModifiedTime(entryToUnmark, originalLastModifiedTime);
        } catch (EntryNotFoundException enfe) {
            assert false : "The target entry cannot be missing";
        }
        setRedoable();
        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, entryToUnmark));
    }
}
