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

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an entry to the list. "
            + "Parameters: TITLE [start/START TIME] [end/END TIME] [#TAG...] [desc/DESCRIPTION]\n"
            + "Example: " + COMMAND_WORD
            + " Buy Banana #NTUC #shopping desc/Bananas are yummy";

    public static final String MESSAGE_SUCCESS = "New entry added: %1$s";
    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the todo list";
    public static final String START_FLAG = "start/";
    public static final String END_FLAG = "end/";
    public static final String TAG_FLAG = "#";
    public static final String DESC_FLAG = "desc/";
    public static final String WRONG_DATE_TIME_INPUT = "Wrong date format supplied. The correct format is YYYY-MM-DD HH:mm";
    public static final Pattern DATE_TIME_FORMAT = Pattern.compile("\\d{4}-\\d{1,2}-\\d{1,2} \\d{2}:\\d{2}");

    private final Entry toAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String title, LocalDateTime startTime, LocalDateTime endTime, Set<String> tags, String description)
            throws IllegalValueException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }

        //@@author A0126539Y
        if (startTime != null && endTime != null) {
            this.toAdd = new Event(
                    new Title(title),
                    startTime,
                    endTime,
                    new UniqueTagList(tagSet),
                    false,
                    description
            );
        }
        else if (startTime == null){
            this.toAdd = new Task(
                    new Title(title),
                    endTime,
                    new UniqueTagList(tagSet),
                    false,
                    description
            );
        } else {
            throw new IllegalValueException(MESSAGE_USAGE);
        }
        //@@author
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_ENTRY);
        }

    }

}
