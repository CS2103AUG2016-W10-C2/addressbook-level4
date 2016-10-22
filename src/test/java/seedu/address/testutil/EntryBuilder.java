package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
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
        for (String tag: tags) {
            entry.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TestEntry build() {
        return this.entry;
    }

}
