package seedu.address.model.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import seedu.address.model.tag.UniqueTagList;
import static seedu.address.commons.core.Messages.SPACE;

/**
 * A read-only immutable interface for an Entry in the Task Manager.
 * Implementations should guarantee: details are present and not null, field
 * values are validated.
 */
public abstract class Entry {
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d 'at' HH:mm");

    protected ObjectProperty<Title> title;
    protected ObjectProperty<UniqueTagList> tags;
    protected BooleanProperty isMarked;
    protected StringProperty description;
    protected ObjectProperty<LocalDateTime> lastModifiedTime;

    String DELIMITER = " ";
    
    /**
     * Get the Title for this Entry
     */
    public final Title getTitle() {
        return title.get();
    }

    /**
     * Sets the Title for this Entry
     */
    public final void setTitle(Title newTitle) {
        title.set(newTitle);
    }

    /**
     * Get the titleObjectProperty for this Entry
     */
    public final ObjectProperty<Title> titleObjectProperty() {
        return title;
    }

    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the task's internal tags.
     */
    public final UniqueTagList getTags() {
        return new UniqueTagList(tags.get());
    }

    /**
     * Sets the Tags for this Entry
     */
    public final void setTags(UniqueTagList newTags) {
        tags.set(new UniqueTagList(newTags));
    }

    /**
     * Add tags for this entry
     */
    public final void addTags(UniqueTagList uniqueTagList) {
        UniqueTagList updatedTagList = new UniqueTagList(tags.get());
        updatedTagList.mergeFrom(uniqueTagList);
        tags.set(updatedTagList);
    }

    /**
     * Remove tags for this entry
     */
    public final void removeTags(UniqueTagList tagsToRemove) {
        UniqueTagList updatedTagList = new UniqueTagList(tags.get());
        updatedTagList.removeFrom(tagsToRemove);
        tags.set(updatedTagList);
    }

    /**
     * Get the uniqueTagListObjectProperty for this Entry
     */
    public final ObjectProperty<UniqueTagList> uniqueTagListObjectProperty() {
        return tags;
    }

    /**
     * Get the description for this Entry
     */
    public final String getDescription() {
        if (description == null){
            return "";
        }
        return description.get();
    }

    /**
     * Set the description for this Entry
     */
    public final void setDescription(String newDescription) {
        if (description == null) {
            description = new SimpleStringProperty(newDescription);
            return;
        }
        description.set(newDescription);
    }

    /**
     * Get the descriptionProperty for this Entry
     */
    public final StringProperty descriptionProperty() {
        return description;
    }

    /**
     * Returns true if Entry is marked as completed
     */
    public boolean isMarked() {
        return isMarked.get();
    }

    /**
     * Marks the entry as completed.
     */
    public final void mark() {
        this.isMarked.set(true);
    }

    /**
     * Unmarks the entry
     */
    public final void unmark() {
        this.isMarked.set(false);
    }

    /**
     * Get the isMarkProperty for this Entry
     */
    public final BooleanProperty isMarkedProperty() {
        return isMarked;
    }
    /**
     * Returns a string representation of this Entry's marking
     */
    public final String markString() {
        return isMarked() ? "[X] " : "[ ] ";
    }

    /**
     * Get the date for display to the user
     */
    public abstract String getDateDisplay(LocalDateTime dateTime);

    //@@author A0116603R
    /**
     * Formats the Entry as text, showing all contact details.
     */
    public String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle());

        if (!getDescription().isEmpty()) {
            builder.append(SPACE);
            builder.append("(");
            builder.append(getDescription());
            builder.append(")");
        }

        if (!getTags().isEmpty()) {
            builder.append(SPACE);
            builder.append(tagsString());
        }
        return builder.toString();
    }

    /**
     * Returns a string representation of this Entry's tags
     */
    public final String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        getTags().forEach(tag -> buffer.append(tag).append(DELIMITER));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - DELIMITER.length());
        }
    }

    //@@author
    /**
     * returns a comparable time used to determine an entry's order
     */
    public abstract LocalDateTime getComparableTime();
    
    /**
     *  sets the last modified time
     */
    public void setLastModifiedTime(LocalDateTime localDateTime) {
        lastModifiedTime.set(localDateTime);
    }
    
    public void resetLastModifiedTime() {
        lastModifiedTime.set(LocalDateTime.now());
    }
    
    public LocalDateTime getLastModifiedTime() {
        return lastModifiedTime.get();
    }

    public ObjectProperty<LocalDateTime> getLastModifiedTimeProperty() {
        return lastModifiedTime;
    }
}
