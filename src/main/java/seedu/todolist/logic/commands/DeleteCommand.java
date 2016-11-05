package seedu.todolist.logic.commands;

import seedu.todolist.commons.core.Messages;
import seedu.todolist.commons.core.UnmodifiableObservableList;
import seedu.todolist.model.task.Entry;
import seedu.todolist.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.todolist.model.task.UniqueTaskList.EntryNotFoundException;

/**
 * Deletes a task identified using it's last displayed index from the todolist book.
 */
public class DeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the entry identified by the index number used in the last entry listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Entry: %1$s";
    public static final String MESSAGE_UNDO_DELETE_PERSON_SUCCESS = "Undo delete Entry: %1$s";
    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the todo list";

    public final int targetIndex;
    public Entry entryToDelete;

    public DeleteCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() {
        if (getCommandState()==CommandState.PRE_EXECUTION){
            UnmodifiableObservableList<Entry> lastShownList = model.getFilteredPersonList();

            if (lastShownList.size() < targetIndex) {
                indicateAttemptToExecuteIncorrectCommand();
                return new CommandResult(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
            }

            entryToDelete = lastShownList.get(targetIndex - 1);
        }

        try {
            model.deleteTask(entryToDelete);
        } catch (EntryNotFoundException enfe) {
            assert false : "The target entry cannot be missing";
        }

        setUndoable();
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, entryToDelete));
    }

    @Override
    //@@author A0121501E
    public CommandResult unexecute() {
        if (getCommandState()!=CommandState.UNDOABLE){
            return new CommandResult(MESSAGE_UNDO_FAIL);
        }
        assert model != null;
        assert entryToDelete != null;
        try {
            model.addTask(entryToDelete);
            setRedoable();
            return new CommandResult(String.format(MESSAGE_UNDO_DELETE_PERSON_SUCCESS, entryToDelete));
        } catch (DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_ENTRY);
        }
    }
}
