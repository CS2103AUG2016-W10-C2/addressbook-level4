package seedu.address.testutil;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;

import java.util.StringJoiner;

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
    public int compareTo(Entry o) {
        return 0;
    }
}
