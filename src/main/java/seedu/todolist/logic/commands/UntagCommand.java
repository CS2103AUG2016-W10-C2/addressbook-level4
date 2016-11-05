package seedu.todolist.logic.commands;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import seedu.todolist.commons.core.Messages;
import seedu.todolist.commons.core.UnmodifiableObservableList;
import seedu.todolist.commons.exceptions.IllegalValueException;
import seedu.todolist.model.task.Entry;
import seedu.todolist.model.task.UniqueTaskList.EntryNotFoundException;
import seedu.todolist.model.tag.Tag;
import seedu.todolist.model.tag.UniqueTagList;

//@@author A0121501E
/*
 * Remove tags from an entry.
 */
public class UntagCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "untag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes tags from task. "
            + "Parameters: TASK_ID TAG[,...] "
            + "Example: " + COMMAND_WORD + " 2 #shopping #food";

    public static final String MESSAGE_SUCCESS = "Removed %1$s from entry: %2$s";
    public static final String MESSAGE_UNDO_SUCCESS = "Undo remove %1$s from entry: %2$s";
    public static final String MESSAGE_NON_EXISTENT = "None of the specified tags exist in the entry: %1$s";

    private final int targetIndex;
    private Entry taskToUntag;
    private LocalDateTime originalLastModifiedTime;

    private final UniqueTagList tagsToRemove;

    public UntagCommand(int targetIndex, Set<String> tags) throws IllegalValueException {
        this.targetIndex = targetIndex;

        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(tagName));
        }
        this.tagsToRemove = new UniqueTagList(tagSet);
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        assert !tagsToRemove.isEmpty(); //should be handled in the parser
        if (getCommandState()==CommandState.PRE_EXECUTION) {
            UnmodifiableObservableList<Entry> lastShownList = model.getFilteredPersonList();
            
            if (lastShownList.size() < targetIndex) {
                indicateAttemptToExecuteIncorrectCommand();
                return new CommandResult(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
            }
            
            taskToUntag = lastShownList.get(targetIndex - 1);
            originalLastModifiedTime = taskToUntag.getLastModifiedTime();
            tagsToRemove.retainAll(taskToUntag.getTags());
        }

        if (tagsToRemove.isEmpty()){
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(String.format(MESSAGE_NON_EXISTENT, taskToUntag));
        }
        try {
            model.untagTask(taskToUntag, tagsToRemove);
        } catch (EntryNotFoundException e) {
            assert false : "The target entry cannot be missing";
        }
        setUndoable();
        return new CommandResult(String.format(MESSAGE_SUCCESS, tagsToRemove, taskToUntag));
    }

    @Override
    public CommandResult unexecute() {
        if (getCommandState() != CommandState.UNDOABLE){
            return new CommandResult(MESSAGE_UNDO_FAIL);
        }
        assert model != null;
        assert taskToUntag != null;
        assert tagsToRemove != null;
        
        try {
            model.tagTask(taskToUntag, tagsToRemove);
            model.updateLastModifiedTime(taskToUntag, originalLastModifiedTime);
        } catch (EntryNotFoundException enfe) {
            assert false : "The target entry cannot be missing";
        }
        setRedoable();
        return new CommandResult(String.format(MESSAGE_UNDO_SUCCESS, tagsToRemove, taskToUntag));
    }

}
