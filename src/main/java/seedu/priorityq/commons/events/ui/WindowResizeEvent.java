package seedu.priorityq.commons.events.ui;

import seedu.priorityq.commons.events.BaseEvent;

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

    /**
     * Returns the new width that the application window was resized to.
     */
    public Double getNewWidth() {
        return newWidth;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
