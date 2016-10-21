package seedu.address.testutil;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
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

    public TestEntry() {
        title = new SimpleObjectProperty<>();
        tags = new SimpleObjectProperty<>(new UniqueTagList());
        description = new SimpleStringProperty("");
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
        // TODO Auto-generated method stub
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
            sb.append(TAG_FLAG);
        }
        StringJoiner sj = new StringJoiner(",");
        this.getTags().getInternalList().stream().forEach(s -> sj.add(s.tagName));
        sb.append(sj.toString());
        return sb.toString();
    }

    @Override
    public boolean isMarked() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void mark() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unmark() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String markString() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addTags(UniqueTagList uniqueTagList) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeTags(UniqueTagList tagsToRemove) {
        // TODO Auto-generated method stub
        
    }
}
