package seedu.todolist.storage;

import seedu.todolist.commons.events.model.TaskManagerChangedEvent;
import seedu.todolist.commons.events.storage.DataSavingExceptionEvent;
import seedu.todolist.commons.exceptions.DataConversionException;
import seedu.todolist.model.ReadOnlyTaskManager;
import seedu.todolist.model.UserPrefs;

import java.io.IOException;
import java.util.Optional;

/**
 * API of the Storage component
 */
public interface Storage extends TaskManagerStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getTaskManagerFilePath();

    @Override
    Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException;

    @Override
    void saveTaskManager(ReadOnlyTaskManager addressBook) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleAddressBookChangedEvent(TaskManagerChangedEvent abce);
}