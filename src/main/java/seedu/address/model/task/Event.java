package seedu.address.model.task;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.swing.plaf.synth.SynthSeparatorUI;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

import static seedu.address.commons.core.Messages.SPACE;

//@@author A0126539Y
public final class Event extends Entry{
    protected ObjectProperty<LocalDateTime> startTime;
    protected ObjectProperty<LocalDateTime> endTime;

    public Event(Title title, LocalDateTime startTime, LocalDateTime endTime, UniqueTagList tags, boolean isMarked, String description) {
        assert !CollectionUtil.isAnyNull(title, tags, description, startTime, endTime);
        this.title = new SimpleObjectProperty<>(Title.copy(title));
        this.tags = new SimpleObjectProperty<>(new UniqueTagList(tags));
        this.isMarked = new SimpleBooleanProperty(Boolean.valueOf(isMarked));
        this.description = new SimpleStringProperty(description);
        this.startTime = new SimpleObjectProperty<>(startTime);
        this.endTime = new SimpleObjectProperty<>(endTime);
    }

    public Event(Entry entry) {
        this(entry.getTitle(), ((Event)entry).getStartTime(), ((Event)entry).getEndTime(), entry.getTags(), entry.isMarked(), entry.getDescription());
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

    @Override
    //@@author A0116603R
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

}
