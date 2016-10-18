package seedu.address.testutil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;

/**
 * A mutable task object. For testing only.
 */
public class TestEntry implements Entry {

    private ObjectProperty<Title> title;
    private ObjectProperty<UniqueTagList> tags;

    public TestEntry() {
        title = new SimpleObjectProperty<>();
        tags = new SimpleObjectProperty<>(new UniqueTagList());
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
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getTitle().fullTitle + " ");
        this.getTags().getInternalList().stream().forEach(s -> sb.append("t/" + s.tagName + " "));
        return sb.toString();
    }

    @Override
    public String getAsText() {
        // TODO Auto-generated method stub
        return null;
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
