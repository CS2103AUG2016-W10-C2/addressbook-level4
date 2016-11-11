package seedu.priorityq.logic.commands;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import seedu.priorityq.commons.core.Messages;
import seedu.priorityq.commons.core.UnmodifiableObservableList;
import seedu.priorityq.commons.exceptions.IllegalValueException;
import seedu.priorityq.model.entry.Entry;
import seedu.priorityq.model.entry.Task;
import seedu.priorityq.model.entry.Title;
import seedu.priorityq.model.entry.Update;
import seedu.priorityq.model.entry.UniqueTaskList.EntryConversionException;
import seedu.priorityq.model.entry.UniqueTaskList.EntryNotFoundException;
import seedu.priorityq.model.tag.Tag;
import seedu.priorityq.model.tag.UniqueTagList;

//@@author A0126539Y 
/*
 * Edit a task's content.
 */
public class EditCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits tasks details. " + "Parameters: TASK_ID"
            + " [TITLE] " + "Example: " + COMMAND_WORD + " 2 Buy bread";

    public static final String MESSAGE_SUCCESS = "Edited entry: %1$s";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo edits to entry: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This entry already exists in the todo list";
    public static final String MESSAGE_ENTRY_CONVERSION = "The entry would change type between Task and Event. Please use delete and add instead.";
    public static final String TITLE_FLAG = "title/";

    private final int targetIndex;

    private final Update update;
    private Entry taskToEdit;
    private LocalDateTime originalLastModifiedTime;
    private Update reverseUpdate;
    private LocalDateTime newEndTime;

    public EditCommand(int targetIndex, String title, LocalDateTime startTime, LocalDateTime endTime, Set<String> tags, String description) throws IllegalValueException {
        this.targetIndex = targetIndex;

        Title newTitle = null;
        if (title != null && !title.isEmpty()) {
            newTitle = new Title(title);
        }

        UniqueTagList newTags = null;
        if (tags != null && !tags.isEmpty()) {
            final Set<Tag> tagSet = new HashSet<>();
            for (String tagName : tags) {
                tagSet.add(new Tag(tagName));
            }
            newTags = new UniqueTagList(tagSet);
        }

        String newDescription = null;
        if (description != null && !description.isEmpty()) {
            newDescription = description;
        }

        //make copy of time
        LocalDateTime newStartTime = startTime == null ? null : startTime.plusDays(0);
        newEndTime = endTime == null ? null : endTime.plusDays(0);
        this.update = new Update(newTitle, newStartTime, newEndTime, newTags, newDescription);
    }

    @Override
    public CommandResult execute() {
        UnmodifiableObservableList<Entry> lastShownList = model.getFilteredEntryList();
        if (getCommandState()==CommandState.PRE_EXECUTION) {
            if (lastShownList.size() < targetIndex) {
                indicateAttemptToExecuteIncorrectCommand();
                return new CommandResult(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
            }

            taskToEdit = lastShownList.get(targetIndex - 1);
            originalLastModifiedTime = taskToEdit.getLastModifiedTime();
            update.setTask(taskToEdit);
            if (newEndTime==null && taskToEdit instanceof Task) {
                update.setEndTime(((Task)taskToEdit).getDeadline());
            }
            reverseUpdate = Update.generateUpdateFromEntry(taskToEdit);
            reverseUpdate.setTask(taskToEdit);
        }
        assert model != null;
        try {
            model.editTask(update);
        } catch (EntryNotFoundException e) {
            assert false : "The target entry cannot be missing";
        } catch (EntryConversionException e) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(MESSAGE_ENTRY_CONVERSION);
        }
        setUndoable();
        return new CommandResult(String.format(MESSAGE_SUCCESS, taskToEdit));
    }

    @Override
    //@@author A0121501E
    public CommandResult unexecute() {
        if (getCommandState()!=CommandState.UNDOABLE){
            return new CommandResult(MESSAGE_UNDO_FAIL);
        }
        assert model != null;
        assert reverseUpdate != null;

        try {
            model.editTask(reverseUpdate);
            model.updateLastModifiedTime(taskToEdit, originalLastModifiedTime);
        } catch (EntryNotFoundException e) {
            assert false : "The target entry cannot be missing";
        } catch (EntryConversionException e) {
            assert false: "Undo shouldn't convert Task to Event and vice versa";
        }
        setRedoable();
        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, taskToEdit));
    }
}
