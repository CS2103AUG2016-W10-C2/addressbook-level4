package seedu.address.model.task;

import java.time.LocalDateTime;

import seedu.address.model.tag.UniqueTagList;

/**
 * Represents an Update to an existing Task's properties
 */
public class Update {

    private Entry task;
    private Title newTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private UniqueTagList newTags;
    private String newDescription;

    public Update(Title title, LocalDateTime startTime, LocalDateTime endTime, UniqueTagList tags, String description) {
        this.newTitle = title;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public LocalDateTime getStartTime() {
    	return startTime;
    }

    public LocalDateTime getEndTime() {
    	return endTime;
    }

    public UniqueTagList getNewTags() {
        return newTags;
    }

    public String getNewDescription() {
        return newDescription;
    }

    //@@author A0126539Y
    public static Update generateUpdateFromEntry(Entry entry) {
        if (entry instanceof Task) {
            return new Update(entry.getTitle(), null, ((Task) entry).getDeadline(), entry.getTags(), entry.getDescription());
        }
        if (entry instanceof Event) {
            return new Update(entry.getTitle(), ((Event) entry).getStartTime(), ((Event) entry).getEndTime(), entry.getTags(), entry.getDescription());
        }
        return null;
    }
    //@@author
}
