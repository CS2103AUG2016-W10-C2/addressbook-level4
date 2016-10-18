package seedu.address.model.task;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

import java.util.Objects;

/**
 * Represents a Floating Task in the Task Manager. Guarantees: details are
 * present and not null, field values are validated.
 */
public class FloatingTask implements Entry {

    private ObjectProperty<Title> title;

    private ObjectProperty<UniqueTagList> tags;
    
    private boolean isMarked;

    /**
     * Every field must be present and not null.
     */
    public FloatingTask(Title title, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(title, tags);
        // protect against changes after constructor.
        this.title = new SimpleObjectProperty<>(Title.copy(title));
        this.tags = new SimpleObjectProperty<>(new UniqueTagList(tags));
        this.isMarked = false;
    }   
    
    public FloatingTask(Title title, UniqueTagList tags, boolean isMarked) {
        this(title, tags);
        this.isMarked = isMarked;
    }

    /**
     * Copy constructor.
     */
    public FloatingTask(Entry source) {
        this(source.getTitle(), source.getTags(), source.isMarked());
    }

    @Override
    public final Title getTitle() {
        return title.get();
    }

    @Override
    public final void setTitle(Title newTitle) {
        this.title.set(newTitle);
    }

    @Override
    public ObjectProperty<Title> titleObjectProperty() {
        return title;
    }

    @Override
    public final UniqueTagList getTags() {
        return new UniqueTagList(tags.get());
    }

    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    @Override
    public final void setTags(UniqueTagList newTags) {
        tags.set(new UniqueTagList(newTags));
    }

    @Override
    public ObjectProperty<UniqueTagList> uniqueTagListObjectProperty() {
        return tags;
    }
    
    @Override
    public void mark() {
        this.isMarked = true;
    }

    @Override
    public void unmark() {
        this.isMarked = false;
    }
    
    @Override
    public boolean isMarked() {
        return isMarked;
    }
    
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FloatingTask // instanceof handles nulls
                && this.isSameStateAs((FloatingTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(title, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public boolean isSameStateAs(FloatingTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTitle().equals(this.getTitle())
                && other.getTags().equals(this.getTags()));
    }

    @Override
    public String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    @Override
    public String markString() {
        return isMarked() ? "[X] " : "[ ] ";
    }

}
