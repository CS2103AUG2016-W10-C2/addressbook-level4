package seedu.address.logic.commands;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.model.task.Entry;
import seedu.address.model.task.UniqueTaskList.DuplicateTaskException;
import seedu.address.model.task.UniqueTaskList.EntryNotFoundException;

public class UnmarkCommand extends Command {
    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unmarks the entry as completed. "
            + "Identified by the index number used in the last entry listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNMARK_ENTRY_SUCCESS = "Unmarked Entry: %1$s";

    public final int targetIndex;

    public UnmarkCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<Entry> lastShownList = model.getFilteredPersonList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        Entry entryToUnmark = lastShownList.get(targetIndex - 1);

        try {
            model.unmarkTask(entryToUnmark);
        } catch (EntryNotFoundException pnfe) {
            assert false : "The target entry cannot be missing";
        } catch (DuplicateTaskException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new CommandResult(String.format(MESSAGE_UNMARK_ENTRY_SUCCESS, entryToUnmark));
    }
}
