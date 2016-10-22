package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.EntryNotFoundException;

public class MarkCommand extends Command {
    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the entry as completed. "
            + "Identified by the index number used in the last entry listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MARK_ENTRY_SUCCESS = "Marked Entry: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This entry already exists in the todo list";

    public final int targetIndex;

    public MarkCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<Entry> lastShownList = model.getFilteredPersonList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        Entry entryToMark = lastShownList.get(targetIndex - 1);

        try {
            model.markTask(entryToMark);
        } catch (EntryNotFoundException pnfe) {
            assert false : "The target entry cannot be missing";
        } catch (DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }

        return new CommandResult(String.format(MESSAGE_MARK_ENTRY_SUCCESS, entryToMark));
    }
}
