package seedu.address.model.task;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import seedu.address.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for an Entry in the Task Manager.
 * Implementations should guarantee: details are present and not null, field
 * values are validated.
 */
public interface Entry {

    /**
     * Get the Title for this Entry
     */
    Title getTitle();

    /**
     * Sets the Title for this Entry
     */
    void setTitle(Title newTitle);

    /**
     * Get the titleObjectProperty for this Entry
     */
    Observable titleObjectProperty();

    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Sets the Tags for this Entry
     */
    void setTags(UniqueTagList uniqueTagList);
    
    /**
     * Add tags for this entry
     */
    void addTags(UniqueTagList uniqueTagList);

    /**
     * Remove tags for this entry
     */
    void removeTags(UniqueTagList tagsToRemove);
    
    /**
     * Get the uniqueTagListObjectProperty for this Entry
     */
    Observable uniqueTagListObjectProperty();

    /**
     * Get the description for this Entry
     */
    String getDescription();

    /**
     * Set the description for this Entry
     */
    void setDescription(String newDescription);

    /**
     * Get the descriptionProperty for this Entry
     */
    Observable descriptionProperty();
    
    /**
     * Get the deadlineProperty for this Entry
     */
    default Observable deadlineObjectProperty() {
    	return null;
    }

    /**
     * Returns true if Entry is marked as completed
     */
    boolean isMarked();
    /**
     * Formats the Entry as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle());
        if (!getTags().isEmpty()) {
            builder.append(" Tags: ");
            getTags().forEach(builder::append);
        }
        if (!getDescription().isEmpty()) {
            builder.append(" Description: ");
            builder.append(getDescription());
        }
        return builder.toString();
    }

    /**
     * Returns a string representation of this Entry's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }



    /**
     * Marks the entry as completed.
     */
    void mark();
    
    /**
     * Unmarks the entry
     */
    void unmark();
    
    /**
     * Get the isMarkProperty for this Entry
     */
    Observable isMarkProperty();
    
    /**
     * Returns a string representation of this Entry's marking
     */
    String markString();

}
