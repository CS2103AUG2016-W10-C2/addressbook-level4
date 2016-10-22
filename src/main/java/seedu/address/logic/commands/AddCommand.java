package seedu.address.logic.commands;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.*;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Adds a task to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a floating task to the list. "
            + "Parameters: TITLE [t/TAG1,TAG2...] [desc/DESCRIPTION]\n"
            + "Example: " + COMMAND_WORD
            + " Buy Banana t/NTUC,shopping desc/Bananas are yummy";

    public static final String MESSAGE_SUCCESS = "New entry added: %1$s";
    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the todo list";
    public static final String DEADLINE_FLAG = "dl/";
    public static final String TAG_FLAG = "#";
    public static final String DESC_FLAG = "desc/";
    public static final String WRONG_DATE_TIME_INPUT = "Wrong date format supplied. The correct format is YYYY-MM-DD HH:mm";
    public static final Pattern DATE_TIME_FORMAT = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2} \\d{2}:\\d{2}");

    private final Task toAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    // TODO: Implement Add for other types of entry
    public AddCommand(String title, LocalDateTime deadline, Set<String> tags, String description)
            throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.toAdd = new Task(
                new Title(title),
                deadline,
                new UniqueTagList(tagSet),
                false,
                description
        );
    }

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    // TODO: Implement Add for other types of entry
    /* public AddCommand(String title, LocalDateTime deadline, Set<String> tags, String description)
            throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.toAdd = new Deadline(
                new Title(title),
                deadline,
                new UniqueTagList(tagSet),
                description
        );
    } */

    public AddCommand(String title, Set<String> tags, String description) throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.toAdd = new Task(new Title(title), null, new UniqueTagList(tagSet), false, description);
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniquePersonList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_ENTRY);
        }

    }

}
