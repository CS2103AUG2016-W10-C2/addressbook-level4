package seedu.address.storage;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import javax.xml.bind.annotation.XmlElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JAXB-friendly version of the Person.
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
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    @XmlElement
    private boolean isMarked;

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedEntry() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedEntry(Entry source) {
        title = source.getTitle().fullTitle;
        description = source.getDescription();
        isMarked = source.isMarked();
        tagged = new ArrayList<>();
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
        }
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
    public Entry toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }
        final Title title = new Title(this.title);
        final UniqueTagList tags = new UniqueTagList(personTags);

        LocalDateTime startTime = start == null || start.isEmpty() ? null : LocalDateTime.parse(start);
        LocalDateTime endTime = end == null || end.isEmpty() ? null : LocalDateTime.parse(end);

        if (startTime != null) {
            return new Event(title, startTime, endTime, tags, isMarked, description, -1);
        }
        return new Task(title, endTime, tags, isMarked, description);
    }
}
