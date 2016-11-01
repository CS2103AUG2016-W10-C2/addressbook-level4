package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author A0116603R
/**
 * An event indicating that the user has finished resizing the
 * application window.
 */
public class WindowResizeEvent extends BaseEvent {
    private Double newWidth;

    public WindowResizeEvent(Number newWidth) {
        this.newWidth = (Double)newWidth;
    }

    public Double getNewWidth() {
        return newWidth;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
