package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.Model;
import seedu.address.model.UserPrefs;

/**
 * 
 * Command to change user preference.
 *
 */
//@@author A0126539Y
public class OptionCommand extends Command{
    public static final String COMMAND_WORD = "option";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Change user preference. "
            + "Parameters: [save/Save Location]\n"
            + "Example: " + COMMAND_WORD
            + " save/myFileName.xml";

    public static final String MESSAGE_SUCCESS_PREFIX = "Preference saved: ";
    public static final String MESSAGE_SAVE_LOCATION_SUCCESS = "Save Location";
    public static final String SAVE_LOCATION_FLAG = "save/";
    
    private UserPrefs toChange;
    private final String saveTargetLocation;
    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public OptionCommand(String saveLocation)
            throws IllegalValueException {
        if (!CollectionUtil.isAnyNotNull(saveLocation)) {
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
        StringBuilder messageBuilder = new StringBuilder();
        if (saveTargetLocation != null && !saveTargetLocation.isEmpty()) {
            toChange.setSaveLocation(saveTargetLocation);
            messageBuilder.append(MESSAGE_SAVE_LOCATION_SUCCESS);
        }
        return new CommandResult(messageBuilder.toString());
    }

}
