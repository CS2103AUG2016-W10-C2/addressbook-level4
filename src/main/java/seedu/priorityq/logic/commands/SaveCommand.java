package seedu.priorityq.logic.commands;

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
public class SaveCommand extends Command{
    public static final String COMMAND_WORD = "save";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Change data save location. "
            + "Parameters: SAVE LOCATION\n"
            + "Example: " + COMMAND_WORD
            + " folder/myFileName.xml";

    public static final String MESSAGE_SUCCESS_PREFIX = "Save location updated to: ";
    private static final String EXTENSION_FINDER_REGEX = "\\.(?=[^\\.]+$)";

    private UserPrefs toChange;
    private final String saveTargetLocation;
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public SaveCommand(String saveLocation)
            throws IllegalValueException, InvalidPathException {
        if (saveLocation != null && !saveLocation.isEmpty()) {
            //verify saveLocation has path format
            Path path = Paths.get(saveLocation);
            String[] pathToken = path.getFileName().toString().split(EXTENSION_FINDER_REGEX);
            if (pathToken.length <= 1 || !pathToken[pathToken.length - 1].equals("xml")) {
                throw new InvalidPathException(saveLocation, "Filepath should has xml extension");
            }
        } else {
            throw new IllegalValueException(MESSAGE_USAGE);
        }
        this.saveTargetLocation = saveLocation;
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
        toChange.setSaveLocation(saveTargetLocation);
        StringBuilder messageBuilder = new StringBuilder().append(MESSAGE_SUCCESS_PREFIX).append(saveTargetLocation);
        return new CommandResult(messageBuilder.toString());
    }

}
