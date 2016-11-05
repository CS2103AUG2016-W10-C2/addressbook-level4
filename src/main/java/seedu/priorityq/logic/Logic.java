package seedu.priorityq.logic;

import javafx.collections.ObservableList;
import seedu.priorityq.logic.commands.CommandHistory;
import seedu.priorityq.logic.commands.CommandResult;
import seedu.priorityq.model.task.Entry;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     */
    CommandResult execute(String commandText);

    /** Returns the filtered list of entries */
    ObservableList<Entry> getFilteredEntryList();

    /** Get the command history manager associated to this Logic instance */
    CommandHistory getCommandHistoryManager();
}
