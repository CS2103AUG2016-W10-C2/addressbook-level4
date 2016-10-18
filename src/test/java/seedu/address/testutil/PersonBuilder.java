package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.*;

/**
 *
 */
public class PersonBuilder {

    private TestEntry person;

    public PersonBuilder() {
        this.person = new TestEntry();
    }

    public PersonBuilder withName(String title) throws IllegalValueException {
        this.person.setTitle(new Title(title));
        return this;
    }

    public PersonBuilder withTags(String ... tags) throws IllegalValueException {
        for (String tag: tags) {
            person.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TestEntry build() {
        return this.person;
    }

}
