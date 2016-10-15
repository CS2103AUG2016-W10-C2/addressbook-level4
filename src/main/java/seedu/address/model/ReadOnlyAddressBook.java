package seedu.address.model;


import seedu.address.model.task.Entry;
import seedu.address.model.task.UniquePersonList;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.util.List;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    UniqueTagList getUniqueTagList();

    UniquePersonList getUniquePersonList();

    /**
     * Returns an unmodifiable view of persons list
     */
    List<Entry> getPersonList();

    /**
     * Returns an unmodifiable view of tags list
     */
    List<Tag> getTagList();

}
