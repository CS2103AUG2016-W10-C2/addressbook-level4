package seedu.address.model.task;

import java.time.LocalDateTime;
import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

public final class Deadline extends Task{
    protected ObjectProperty<LocalDateTime> deadline;

	public Deadline(Title title, LocalDateTime deadline, UniqueTagList tags, String desc) {
		this(title, deadline, tags, false, desc);
	}

	public Deadline(Title title, LocalDateTime deadline, UniqueTagList tags, boolean isMarked, String desc) {
		super(title, null, tags, isMarked, desc);
        assert !CollectionUtil.isAnyNull(deadline);
		this.deadline = new SimpleObjectProperty<>(deadline);
	}
	
	public Deadline(Entry entry) {
		super(entry);
		assert entry instanceof Deadline;
		this.deadline  = new SimpleObjectProperty<>(((Deadline)entry).getDeadline());
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
	public int hashCode() {
	  // use this method for custom fields hashing instead of implementing your own
	  return Objects.hash(title, tags, deadline);
	}

	@Override
    public String toString() {
        return getAsText();
    }

    public boolean isSameStateAs(Deadline other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTitle().equals(this.getTitle())
				&& other.getTags().equals(this.getTags())
				&& other.getDeadline().equals(this.getDeadline()));
    }

	@Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                && this.isSameStateAs((Deadline) other));
    }

    @Override
    public String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle())
				.append(" Deadline: ")
				.append(getDeadline().toString())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }
    
}