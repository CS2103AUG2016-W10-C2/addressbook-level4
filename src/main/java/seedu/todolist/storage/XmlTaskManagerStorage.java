package seedu.todolist.storage;

import seedu.todolist.commons.core.LogsCenter;
import seedu.todolist.commons.exceptions.DataConversionException;
import seedu.todolist.commons.util.FileUtil;
import seedu.todolist.model.ReadOnlyTaskManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A class to access TaskManager data stored as an xml file on the hard disk.
 */
public class XmlTaskManagerStorage implements TaskManagerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTaskManagerStorage.class);

    private String filePath;

    public XmlTaskManagerStorage(String filePath){
        this.filePath = filePath;
    }

    public String getTaskManagerFilePath(){
        return filePath;
    }
    
    public void setTaskManagerFilepath(String filepath) {
        this.filePath = filepath;
    }

    /**
     * Similar to {@link #readTaskManager()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyTaskManager> readTaskManager(String filePath) throws DataConversionException, FileNotFoundException {
        assert filePath != null;

        File addressBookFile = new File(filePath);

        if (!addressBookFile.exists()) {
            logger.info("TaskManager file "  + addressBookFile + " not found");
            return Optional.empty();
        }

        ReadOnlyTaskManager addressBookOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(addressBookOptional);
    }

    /**
     * Similar to {@link #saveTaskManager(ReadOnlyTaskManager)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveTaskManager(ReadOnlyTaskManager addressBook, String filePath) throws IOException {
        assert addressBook != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableTaskManager(addressBook));
    }

    @Override
    public Optional<ReadOnlyTaskManager> readTaskManager() throws DataConversionException, IOException {
        return readTaskManager(filePath);
    }

    @Override
    public void saveTaskManager(ReadOnlyTaskManager addressBook) throws IOException {
        saveTaskManager(addressBook, filePath);
    }
}