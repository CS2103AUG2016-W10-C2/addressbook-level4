package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An event to indicate that the user has requested to focus on
 * the command line.
 */
public class FocusCommandLineEvent extends BaseEvent {

    @Override
    public String toString() {
        return "User requested to focus on command line.";
    }
}
