package seedu.priorityq.logic.commands;

import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.task.*;
import seedu.priorityq.model.task.UniqueTaskList.EntryNotFoundException;
import seedu.priorityq.model.tag.Tag;
import seedu.priorityq.model.tag.UniqueTagList;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Adds a task to the task manager.
 */
public class AddCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an entry to the list.\n"
            + "Parameters: TITLE [start/START TIME] [end/END TIME] [#TAG...] [desc/DESCRIPTION]\n"
            + "Example: " + COMMAND_WORD
            + " Buy Banana #NTUC #shopping desc/Bananas are yummy";

    public static final String MESSAGE_SUCCESS = "New entry added: %1$s";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo add new entry: %1$s";
    public static final String MESSAGE_DUPLICATE_ENTRY = "This entry already exists in the todo list";
    public static final String MESSAGE_START_END_ERROR = "The start/ flag must be used in conjunction with the end/ flag";
    public static final String START_FLAG = "start/";
    public static final String END_FLAG = "end/";
    public static final String TAG_FLAG = "#";
    public static final String DESC_FLAG = "desc/";
    public static final String REPEAT_FLAG = "repeat/";
    public static final String WRONG_DATE_TIME_INPUT = "Wrong date time format: %s. Try 'tomorrow' or '2016-10-10'.";

    private final Entry toAdd;


    //@@author A0126539Y
    /**
     * Convenience constructor using raw values.
     * @param recursion TODO
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(String title, LocalDateTime startTime, LocalDateTime endTime, Set<String> tags, String description, long recursion)
            throws IllegalValueException, IllegalArgumentException {
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }

        if (startTime != null && endTime != null) {
            this.toAdd = new Event(
                    new Title(title),
                    startTime,
                    endTime,
                    new UniqueTagList(tagSet),
                    false,
                    description,
                    recursion,
                    LocalDateTime.now()
            );
        }
        else if (startTime == null){
            this.toAdd = new Task(
                    new Title(title),
                    endTime,
                    new UniqueTagList(tagSet),
                    false,
                    description,
                    LocalDateTime.now()
            );
        }
        else if (startTime != null && endTime == null) {
            throw new IllegalValueException(MESSAGE_START_END_ERROR);
        }
        else {
            throw new IllegalValueException(MESSAGE_USAGE);
        }
    }
    //@@author

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd);
            setUndoable();
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_ENTRY);
        }

    }
    //@@author A0121501E
    @Override
    public CommandResult unexecute() {
        if (getCommandState()!=CommandState.UNDOABLE){
            return new CommandResult(MESSAGE_UNDO_FAIL);
        }
        assert toAdd != null;
        try {
            model.deleteTask(toAdd);
        } catch (EntryNotFoundException enfe) {
            assert false : "The target entry cannot be missing";
        }
        setRedoable();
        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, toAdd));
    }
}
