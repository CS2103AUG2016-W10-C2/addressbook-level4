package seedu.address.testutil;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;

import java.time.LocalDateTime;

import static seedu.address.logic.commands.AddCommand.TAG_FLAG;

/**
 * A mutable task object. For testing only.
 */
public class TestEntry extends Entry {
    public TestEntry() {
        title = new SimpleObjectProperty<>();
        tags = new SimpleObjectProperty<>(new UniqueTagList());
        description = new SimpleStringProperty("");
        isMarked = new SimpleBooleanProperty(false);
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getTitle().fullTitle + " ");
        if (!this.getTags().isEmpty()) {
            this.getTags().getInternalList().forEach(s -> sb.append(" " + TAG_FLAG).append(s.tagName));
        }
        return sb.toString();
    }
    @Override
    public String getDateDisplay(LocalDateTime dateTime) {
        return "";
    }
}
