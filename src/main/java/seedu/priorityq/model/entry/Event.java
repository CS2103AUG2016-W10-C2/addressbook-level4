package seedu.priorityq.model.entry;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import seedu.priorityq.commons.util.CollectionUtil;
import seedu.priorityq.model.tag.UniqueTagList;

import static seedu.priorityq.commons.core.Messages.SPACE;

//@@author A0126539Y
public final class Event extends Entry{
    protected ObjectProperty<LocalDateTime> startTime;
    protected ObjectProperty<LocalDateTime> endTime;
    protected SimpleLongProperty recursion; // value is in milis
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("EEE, MMM d 'at' HH:mm");
    private static final String INVALID_START_END_TIME = "Invalid start and end time. i.e: start time is after end time.";

    private void convertToNextRecursion() {
        if (recursion.get() > 0  && getEndTime().compareTo(LocalDateTime.now()) < 0) {
            //get the length of event in seconds unit
            long eventLengthSeconds = getStartTime().until(getEndTime(), ChronoUnit.SECONDS);

            LocalDateTime newStartTime = startTime.getValue();
            while (newStartTime.compareTo(LocalDateTime.now()) < 0) {
                newStartTime = newStartTime.plusSeconds(recursion.getValue() / 1000);
            }

            LocalDateTime newEndTime = newStartTime.plusSeconds(eventLengthSeconds);
            startTime = new SimpleObjectProperty<>(newStartTime);
            endTime = new SimpleObjectProperty<>(newEndTime);
        }
    }

    public Event(Title title, LocalDateTime startTime, LocalDateTime endTime, UniqueTagList tags, boolean isMarked, String description, long recursion, LocalDateTime lastModifiedTime) throws IllegalArgumentException{
        assert !CollectionUtil.isAnyNull(title, tags, description, startTime, endTime, lastModifiedTime);
        if(startTime.isAfter(endTime)) {
            throw new IllegalArgumentException(INVALID_START_END_TIME);
        }
        this.title = new SimpleObjectProperty<>(Title.copy(title));
        this.tags = new SimpleObjectProperty<>(new UniqueTagList(tags));
        this.isMarked = new SimpleBooleanProperty(Boolean.valueOf(isMarked));
        this.description = new SimpleStringProperty(description);
        this.startTime = new SimpleObjectProperty<>(startTime);
        this.endTime = new SimpleObjectProperty<>(endTime);
        this.recursion = recursion <= 0 ? new SimpleLongProperty() : new SimpleLongProperty(recursion);
        this.lastModifiedTime = new SimpleObjectProperty<>(lastModifiedTime);

        convertToNextRecursion();
    }

    public Event(Entry entry) throws IllegalArgumentException {
        this(entry.getTitle(), ((Event)entry).getStartTime(), ((Event)entry).getEndTime(), entry.getTags(), entry.isMarked(), entry.getDescription(), -1, entry.getLastModifiedTime());
    }

    public LocalDateTime getStartTime() {
        return startTime.get();
    }

    public String getStartTimeDisplay() {
        return getDateDisplay(getStartTime());
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime.set(startTime);
    }

    public LocalDateTime getEndTime() {
        return endTime.get();
    }

    public String getEndTimeDisplay() {
        return getDateDisplay(getEndTime());
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime.set(endTime);
    }

    public ObjectProperty<LocalDateTime> startTimeObjectProperty() {
        return startTime;
    }

    public ObjectProperty<LocalDateTime> endTimeObjectProperty() {
        return endTime;
    }

    public long getRecursion() {
        return recursion.get();
    }

    public SimpleLongProperty recursionObjectProperty() {
        return recursion;
    }

    public void setRecursion(long recursion) {
        this.recursion = new SimpleLongProperty(recursion);
    }

    @Override
    public int hashCode() {
      // use this method for custom fields hashing instead of implementing your own
      return Objects.hash(title, tags, startTime, endTime);
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public boolean isSameStateAs(Event other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getAsText().equals(this.getAsText())
                && other.isMarked() == this.isMarked());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Event // instanceof handles nulls
                && this.isSameStateAs((Event) other));
    }

    //@@author A0116603R
    @Override
    public String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.getAsText());

        assert (getStartTime() != null && getEndTime() != null);

        builder.append(SPACE);
        builder.append("from: ");
        builder.append(getStartTimeDisplay());

        builder.append(SPACE);
        builder.append("to: ");
        builder.append(getEndTimeDisplay());
        return builder.toString();
    }

    // @@author A0127828W
    @Override
    protected String getDateDisplay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        Date interpreted = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    // @@author A0121501E
    @Override
    public LocalDateTime getComparableTime() {
        return startTime.get();
    }
    //@@author
    @Override
    public boolean isMarked() {
        return LocalDateTime.now().isAfter(endTime.get());
    }
}
