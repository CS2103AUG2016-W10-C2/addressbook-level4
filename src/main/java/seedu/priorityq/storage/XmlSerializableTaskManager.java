package seedu.priorityq.storage;

import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.tag.Tag;
import seedu.priorityq.model.tag.UniqueTagList;
import seedu.priorityq.model.ReadOnlyTaskManager;
import seedu.priorityq.model.task.Entry;
import seedu.priorityq.model.task.EntryViewComparator;
import seedu.priorityq.model.task.UniqueTaskList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable TaskManager that is serializable to XML format
 */
@XmlRootElement(name = "taskmanager")
public class XmlSerializableTaskManager implements ReadOnlyTaskManager {

    @XmlElement
    private List<XmlAdaptedEntry> persons;
    @XmlElement
    private List<Tag> tags;

    {
        persons = new ArrayList<>();
        tags = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableTaskManager() {}

    /**
     * Conversion
     */
    public XmlSerializableTaskManager(ReadOnlyTaskManager src) {
        persons.addAll(src.getUnsortedTaskList().stream().map(XmlAdaptedEntry::new).collect(Collectors.toList()));
        tags = src.getTagList();
    }

    @Override
    public UniqueTagList getUniqueTagList() {
        try {
            return new UniqueTagList(tags);
        } catch (UniqueTagList.DuplicateTagException e) {
            //TODO: better error handling
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        UniqueTaskList lists = new UniqueTaskList();
        for (XmlAdaptedEntry p : persons) {
            try {
                lists.add(p.toModelType());
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
            }
        }
        return lists;
    }

    @Override
    public List<Entry> getTaskList() {
        List<Entry> list = getUnsortedTaskList();
        Collections.sort(list, new EntryViewComparator());
        return list;
    }

    @Override
    public List<Entry> getUnsortedTaskList() {
        return persons.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public List<Tag> getTagList() {
        return Collections.unmodifiableList(tags);
    }

}
