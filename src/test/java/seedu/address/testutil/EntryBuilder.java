package seedu.address.testutil;

import java.time.LocalDateTime;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.*;

/**
 *
 */
public class EntryBuilder {

    private TestEntry entry;

    public EntryBuilder() {
        this.entry = new TestEntry();
    }

    public EntryBuilder withTitle(String title) throws IllegalValueException {
        this.entry.setTitle(new Title(title));
        return this;
    }

    public EntryBuilder withTags(String ... tags) throws IllegalValueException {
        UniqueTagList uniqueTagList = new UniqueTagList();

        for (String tag: tags) {
            uniqueTagList.add(new Tag(tag));
        }

        entry.setTags(uniqueTagList);
        return this;
    }

    public EntryBuilder withDescription(String description) {
        entry.setDescription(description);
        return this;
    }

    public EntryBuilder withLastModifiedDate(LocalDateTime localDateTime) throws IllegalValueException {
        this.entry.setLastModifiedTime(localDateTime);
        return this;
    }
    
    public TestEntry build() {
        return this.entry;
    }

}
