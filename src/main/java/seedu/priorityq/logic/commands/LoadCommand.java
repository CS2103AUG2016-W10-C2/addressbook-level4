package seedu.priorityq.logic.commands;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.Model;
import seedu.priorityq.model.UserPrefs;

/**
 *
 * Command to change user preference.
 *
 */
//@@author A0126539Y
public class LoadCommand extends Command{
    public static final String COMMAND_WORD = "load";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Change data location. "
            + "Parameters: LOAD_LOCATION\n"
            + "Example: " + COMMAND_WORD
            + " folder/myFileName.xml";

    public static final String MESSAGE_SUCCESS_PREFIX = "Data updated to: ";
    private static final String EXTENSION_FINDER_REGEX = "\\.(?=[^\\.]+$)";

    private UserPrefs toChange;
    private final String loadTargetLocation;
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public LoadCommand(String loadLocation)
            throws IllegalValueException, InvalidPathException {
        if (loadLocation != null && !loadLocation.isEmpty()) {
            //verify saveLocation has path format
            Path path = Paths.get(loadLocation);
            String[] pathToken = path.getFileName().toString().split(EXTENSION_FINDER_REGEX);
            if (pathToken.length <= 1 || !pathToken[pathToken.length - 1].equals("xml")) {
                throw new InvalidPathException(loadLocation, "Filepath should has xml extension");
            }
            File file = new File(loadLocation);
            if (file.isDirectory()) {
                throw new InvalidPathException(loadLocation, "Filepath is a directory");
            }
            if (!file.exists()) {
                throw new InvalidPathException(loadLocation, "Filepath doesn't exist");
            }
        } else {
            throw new IllegalValueException(MESSAGE_USAGE);
        }
        this.loadTargetLocation = loadLocation;
    }

    @Override
    public void setData(Model model) {
       // this method shouldn't be called in option command.
       assert false;
    }

    public void setUserPrefs(UserPrefs userPrefs) {
        this.toChange = userPrefs;
    }

    @Override
    public CommandResult execute() {
        toChange.setSaveLocation(loadTargetLocation);
        StringBuilder messageBuilder = new StringBuilder().append(MESSAGE_SUCCESS_PREFIX).append(loadTargetLocation);
        return new CommandResult(messageBuilder.toString());
    }

}
