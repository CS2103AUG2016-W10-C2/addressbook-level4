package seedu.address.model.task;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

import java.util.Objects;

/**
 * Represents a Floating Task in the Task Manager. Guarantees: details are
 * present and not null, field values are validated.
 */
public class FloatingTask implements Entry {

    private Title title;

    private UniqueTagList tags;
    
    private boolean isMarked;

    /**
     * Every field must be present and not null.
     */
    public FloatingTask(Title title, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(title, tags);
        // protect against changes after constructor.
        this.title = Title.copy(title);
        this.tags = new UniqueTagList(tags);
        this.isMarked = false;
    }   
    
    public FloatingTask(Title title, UniqueTagList tags, boolean isMarked) {
        assert !CollectionUtil.isAnyNull(title, tags);
        this.title = Title.copy(title);
        this.tags = new UniqueTagList(tags);
        this.isMarked = isMarked;
    }

    /**
     * Copy constructor.
     */
    public FloatingTask(Entry source) {
        this(source.getTitle(), source.getTags(), source.isMarked());
    }

    @Override
    public Title getTitle() {
        return title;
    }

    public void setTitle(Title newTitle) {
        this.title = newTitle;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
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
