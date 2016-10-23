package seedu.address.model.task;

import seedu.address.model.tag.UniqueTagList;

/**
 * Represents an Update to an existing Task's properties
 */
public class Update {

    private Entry task;
    private Title newTitle;
    private UniqueTagList newTags;
    private String newDescription;

    public Update(Title title, UniqueTagList tags, String description) {
        this.newTitle = title;
        this.newTags = tags;
        this.newDescription = description;
    }

    public Entry getTask() {
        return task;
    }

    public void setTask(Entry task) {
        this.task = task;
    }

    public Title getNewTitle() {
        return newTitle;
    }

    public UniqueTagList getNewTags() {
        return newTags;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public static Update generateUpdateFromEntry(Entry entry) {
        return new Update(entry.getTitle(), entry.getTags(), entry.getDescription());
    }
}
