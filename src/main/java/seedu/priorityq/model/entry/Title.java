package seedu.priorityq.model.entry;

import seedu.priorityq.commons.exceptions.IllegalValueException;

/**
 * Represents a Entry's name in the task manager.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Title {

    public static final String MESSAGE_NAME_CONSTRAINTS = "Entry title should be spaces or alphanumeric characters. "
                                                          +"(Your input:\"%1$s\")";
    public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum} ]+";

    public final String fullTitle;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Title(String name) throws IllegalValueException {
        assert name != null;
        String trimmedName = name.trim();
        if (!isValidName(trimmedName)) {
            throw new IllegalValueException(String.format(MESSAGE_NAME_CONSTRAINTS, trimmedName));
        }
        this.fullTitle = trimmedName;
    }
    
    public static Title copy(Title original) {
    	try {
        	return new Title(original.fullTitle);
    	} catch (IllegalValueException ive) {
    		assert false: "Original Title has empty fullTitle.";
    	}
		return null;
    }

    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullTitle;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Title // instanceof handles nulls
                && this.fullTitle.equals(((Title) other).fullTitle)); // state check
    }

    @Override
    public int hashCode() {
        return fullTitle.hashCode();
    }

}
