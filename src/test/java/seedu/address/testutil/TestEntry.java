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
public class TestEntry implements Entry {

    private ObjectProperty<Title> title;
    private ObjectProperty<UniqueTagList> tags;
    private StringProperty description;
    protected BooleanProperty isMarked;
    
    public TestEntry() {
        title = new SimpleObjectProperty<>();
        tags = new SimpleObjectProperty<>(new UniqueTagList());
        description = new SimpleStringProperty("");
        isMarked = new SimpleBooleanProperty(false);
    }

    @Override
    public Title getTitle() {
        return title.get();
    }

    @Override
    public void setTitle(Title newTitle) {
        title.set(newTitle);
    }

    @Override
    public ObjectProperty<Title> titleObjectProperty() {
        return title;
    }

    @Override
    public UniqueTagList getTags() {
        return tags.get();
    }

    @Override
    public void setTags(UniqueTagList uniqueTagList) {
        tags.set(new UniqueTagList(uniqueTagList));
    }

    @Override
    public ObjectProperty<UniqueTagList> uniqueTagListObjectProperty() {
        return tags;
    }

    @Override
    public String getDescription() {
        return description.get();
    }

    @Override
    public void setDescription(String newDescription) {
        if (description == null) {
            description = new SimpleStringProperty(newDescription);
            return;
        }
        description.set(newDescription);
    }

    @Override
    public Observable descriptionProperty() {
        return description;
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
    public Observable isMarkProperty() {
        return isMarked;
    }

    @Override
    public void addTags(UniqueTagList uniqueTagList) {
        UniqueTagList updatedTagList = new UniqueTagList(tags.get());
        updatedTagList.mergeFrom(uniqueTagList);
        tags.set(updatedTagList);
    }
    
    @Override
    public void removeTags(UniqueTagList tagsToRemove) {
        UniqueTagList updatedTagList = new UniqueTagList(tags.get());
        updatedTagList.removeFrom(tagsToRemove);
        tags.set(updatedTagList);
    }

    @Override
    public String markString() {
        return isMarked() ? "[X] " : "[ ] ";
    }

}
