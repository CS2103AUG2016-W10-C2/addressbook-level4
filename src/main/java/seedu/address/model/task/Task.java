package seedu.address.model.task;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Floating Task in the Task Manager. Guarantees: details are
 * present and not null, field values are validated.
 */
public class Task implements Entry {

    protected ObjectProperty<Title> title;
    protected ObjectProperty<UniqueTagList> tags;
    protected BooleanProperty isMarked;
    protected ObjectProperty<LocalDateTime> deadline;
    protected StringProperty description;

    public Task(Title title, LocalDateTime deadline, UniqueTagList tags, boolean isMarked, String description) {
        assert !CollectionUtil.isAnyNull(title, tags, description);
        this.title = new SimpleObjectProperty<>(Title.copy(title));
        this.tags = new SimpleObjectProperty<>(new UniqueTagList(tags));
        this.isMarked = new SimpleBooleanProperty(Boolean.valueOf(isMarked));
        this.description = new SimpleStringProperty(description);
        if (deadline != null) {
            this.deadline = new SimpleObjectProperty<>(deadline);
        } else {
        	this.deadline = new SimpleObjectProperty<>();
        }
    }

    /**
     * Every field must be present and not null.
     */
    public Task(Title title, UniqueTagList tags) {
        this(title, null, tags, false, "");
    }

    public Task(Title title, UniqueTagList tags, boolean isMarked) {
        this(title, null, tags, isMarked, "");
    }


    /**
     * Copy constructor.
     */
    public Task(Entry source) {
        this(source.getTitle(), null, source.getTags(), source.isMarked(), source.getDescription());
        if (source instanceof Task) {
        	setDeadline(((Task)source).getDeadline()) ;
        }
    }

    @Override
    public final Title getTitle() {
        return title.get();
    }

    @Override
    public final void setTitle(Title newTitle) {
        title.set(newTitle);
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

    /**
     * Adds every tag from the argument tag list that does not yet exist in this entry's tag list.
     */
    @Override
    public void addTags(UniqueTagList uniqueTagList) {
        UniqueTagList updatedTagList = new UniqueTagList(tags.get());
        updatedTagList.mergeFrom(uniqueTagList);
        tags.set(updatedTagList);
    }

    /**
     * Remove every tag from the argument tag list that exists in this entry's tag list.
     */
    @Override
    public void removeTags(UniqueTagList tagsToRemove) {
        UniqueTagList updatedTagList = new UniqueTagList(tags.get());
        updatedTagList.removeFrom(tagsToRemove);
        tags.set(updatedTagList);
    }

    @Override
    public ObjectProperty<UniqueTagList> uniqueTagListObjectProperty() {
        return tags;
    }

    public LocalDateTime getDeadline() {
		return deadline.get();
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline.set(deadline);
	}

	@Override
	public ObjectProperty<LocalDateTime> deadlineObjectProperty() {
		return deadline;
	}

    @Override
    public final String getDescription() {
        if (description == null){
            return "";
        }
        return description.get();
    }

    @Override
    public final void setDescription(String newDescription) {
        if (description == null) {
            description = new SimpleStringProperty(newDescription);
            return;
        }
        description.set(newDescription);
    }

    @Override
    public StringProperty descriptionProperty() {
        return description;
    }


    @Override
    public void mark() {
        this.isMarked.set(true);
    }

    @Override
    public void unmark() {
        this.isMarked.set(false);
    }

    @Override
    public boolean isMarked() {
        return isMarked.get();
    }

    @Override
    public Observable isMarkedProperty() {
        return isMarked;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Task // instanceof handles nulls
                && this.isSameStateAs((Task) other));
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

    public boolean isSameStateAs(Task other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getAsText().equals(this.getAsText())
                && other.isMarked() == this.isMarked());
    }

    @Override
    public String getAsText() {
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

        if (getDeadline() != null) {
        	builder.append(" Deadline: ");
        	builder.append(getDeadline().toString());
        }
        return builder.toString();
    }

    @Override
    public String markString() {
        return isMarked() ? "[X] " : "[ ] ";
    }

}
