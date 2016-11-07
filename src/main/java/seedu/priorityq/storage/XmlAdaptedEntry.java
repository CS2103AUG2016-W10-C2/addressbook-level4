package seedu.priorityq.storage;

import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.entry.*;
import seedu.priorityq.model.tag.Tag;
import seedu.priorityq.model.tag.UniqueTagList;

import javax.xml.bind.annotation.XmlElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JAXB-friendly version of the Entry.
 */
public class XmlAdaptedEntry {

    @XmlElement(required = true)
    private String title;

    @XmlElement
    private String description = "";

    @XmlElement
    private String start;

    @XmlElement
    private String end;

    @XmlElement
    private String lastModified;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    @XmlElement
    private boolean isMarked;

    @XmlElement
    private long recursion;

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedEntry() {}


    /**
     * Converts a given Entry into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedEntry(Entry source) {
        title = source.getTitle().fullTitle;
        description = source.getDescription();
        isMarked = source.isMarked();
        tagged = new ArrayList<>();
        lastModified = source.getLastModifiedTime().toString();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }

        if (source instanceof Task) {
            LocalDateTime deadlineTime = ((Task)source).getDeadline();
            end = deadlineTime == null ? "" : deadlineTime.toString();
        }

        if (source instanceof Event) {
            Event event = (Event)source;
            start = event.getStartTime().toString();
            end=  event.getEndTime().toString();
            recursion = event.getRecursion();
        }
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Entry object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
    public Entry toModelType() throws IllegalValueException {
        final List<Tag> entryTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            entryTags.add(tag.toModelType());
        }
        final Title title = new Title(this.title);
        final UniqueTagList tags = new UniqueTagList(entryTags);

        LocalDateTime startTime = start == null || start.isEmpty() ? null : LocalDateTime.parse(start);
        LocalDateTime endTime = end == null || end.isEmpty() ? null : LocalDateTime.parse(end);
        LocalDateTime lastModifiedTime = LocalDateTime.parse(lastModified);

        if (startTime != null) {
            return new Event(title, startTime, endTime, tags, isMarked, description, recursion, lastModifiedTime);
        }
        return new Task(title, endTime, tags, isMarked, description, lastModifiedTime);
    }
}
