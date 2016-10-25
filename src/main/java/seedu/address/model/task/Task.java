package seedu.address.model.task;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.ocpsoft.prettytime.PrettyTime;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static seedu.address.commons.core.Messages.SPACE;

/**
 * Represents a Floating Task in the Task Manager. Guarantees: details are
 * present and not null, field values are validated.
 */
//@@author A0126539Y
public class Task extends Entry {
    static final PrettyTime prettyTime = new PrettyTime();
    protected ObjectProperty<LocalDateTime> deadline;


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


    /**
     * Copy constructor.
     */
    public Task(Entry source) {
        this(source.getTitle(), null, source.getTags(), source.isMarked(), source.getDescription());
        if (source instanceof Task) {
            setDeadline(((Task)source).getDeadline()) ;
        }
    }

    public LocalDateTime getDeadline() {
        return deadline.get();
    }

    public String getDeadlineDisplay() {
        return getDateDisplay(getDeadline());
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline.set(deadline);
    }

    public ObjectProperty<LocalDateTime> deadlineObjectProperty() {
        return deadline;
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
    //@@author A0116603R
    public String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.getAsText());
        if (getDeadline() != null) {
            builder.append(SPACE);
            builder.append("Due:");
            builder.append(getDeadlineDisplay());
        }
        return builder.toString();
    }

    @Override
    // @@author A0127828W
    public String getDateDisplay(LocalDateTime dateTime){
        if (dateTime == null) {
            return "";
        }
        Date interpreted = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return prettyTime.format(interpreted);
    }
}
