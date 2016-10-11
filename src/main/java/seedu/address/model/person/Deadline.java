package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.Objects;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

public final class Deadline extends FloatingTask{
	private Title title;

    private UniqueTagList tags;

    private LocalDateTime deadline;

	public Deadline(Title title, LocalDateTime deadline, UniqueTagList tags) {
		super(title, tags);
        assert !CollectionUtil.isAnyNull(deadline);
		this.deadline = deadline;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDateTime deadline) {
		this.deadline = deadline;
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
