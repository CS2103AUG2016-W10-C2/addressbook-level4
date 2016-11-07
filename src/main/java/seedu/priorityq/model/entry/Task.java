package seedu.priorityq.model.entry;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.ocpsoft.prettytime.PrettyTime;
import seedu.priorityq.commons.util.CollectionUtil;
import seedu.priorityq.model.tag.UniqueTagList;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

import static seedu.priorityq.commons.core.Messages.SPACE;

/**
 * Represents a Floating Task in the Task Manager. Guarantees: details are
 * present and not null, field values are validated.
 */
//@@author A0126539Y
public class Task extends Entry {
    private static final PrettyTime dateFormatter = new PrettyTime();
    protected ObjectProperty<LocalDateTime> deadline;


    public Task(Title title, LocalDateTime deadline, UniqueTagList tags, boolean isMarked, String description, LocalDateTime lastModifiedTime) {
        assert !CollectionUtil.isAnyNull(title, tags, description, lastModifiedTime);
        this.title = new SimpleObjectProperty<>(Title.copy(title));
        this.tags = new SimpleObjectProperty<>(new UniqueTagList(tags));
        this.isMarked = new SimpleBooleanProperty(Boolean.valueOf(isMarked));
        this.description = new SimpleStringProperty(description);
        this.deadline = deadline == null ? new SimpleObjectProperty<>() : new SimpleObjectProperty<>(deadline);
        this.lastModifiedTime = new SimpleObjectProperty<>(lastModifiedTime);
    }

    /**
     * Every field must be present and not null.
     */
    public Task(Title title, UniqueTagList tags) {
        this(title, null, tags, false, "", LocalDateTime.MIN);
    }


    /**
     * Copy constructor.
     */
    public Task(Entry source) {
        this(source.getTitle(), null, source.getTags(), source.isMarked(), source.getDescription(), source.getLastModifiedTime());
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
        return Objects.hash(title, tags, deadline, isMarked, description, lastModifiedTime);
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public boolean isSameStateAs(Task other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getAsText().equals(this.getAsText())
                && other.isMarked() == this.isMarked()
                && other.getLastModifiedTime().equals(this.getLastModifiedTime()));
    }

    public boolean isFloatingTask() {
        return deadline.get() == null;
    }

    public PrettyTime getDateFormatter() {
        return dateFormatter;
    }
      
    
    //@@author A0116603R
    @Override
    public String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.getAsText());
        if (getDeadline() != null) {
            builder.append(SPACE);
            builder.append("Due: ");
            builder.append(getDeadlineDisplay());
        }
        return builder.toString();
    }

    @Override
    // @@author A0127828W
    protected String getDateDisplay(LocalDateTime dateTime){
        if (dateTime == null) {
            return "";
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnightYesterday = LocalDateTime.of(now.toLocalDate(), LocalTime.MIDNIGHT);
        LocalDateTime midnightTomorrow = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);
        if (!(dateTime.isAfter(midnightYesterday) && dateTime.isBefore(midnightTomorrow))) {
            return super.getDateDisplay(dateTime);
        }
        Date interpreted = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return dateFormatter.format(interpreted);
    }

    // @@author A0121501E
    @Override
    public LocalDateTime getComparableTime() {
        if (isFloatingTask()) {
            return lastModifiedTime.get();
        }
        return deadline.get();
    }

}
