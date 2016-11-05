package seedu.todolist.logic;

import javafx.collections.ObservableList;
import seedu.todolist.logic.commands.CommandHistory;
import seedu.todolist.logic.commands.CommandResult;
import seedu.todolist.model.task.Entry;

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

    /** Returns the filtered list of persons */
    ObservableList<Entry> getFilteredPersonList();

    /** Get the command history manager associated to this Logic instance */
    CommandHistory getCommandHistoryManager();
}
