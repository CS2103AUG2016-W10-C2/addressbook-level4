package seedu.address.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.UnmodifiableObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.task.Entry;
import seedu.address.model.task.Title;
import seedu.address.model.task.UniquePersonList.DuplicateTaskException;
import seedu.address.model.task.UniquePersonList.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.task.Update;

/*
 * Edit a task's content.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits tasks details. " + "Parameters: TASK_ID"
            + " [TITLE] " + "Example: " + COMMAND_WORD + " 2 Buy bread";

    public static final String MESSAGE_SUCCESS = "Edited entry: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This entry already exists in the todo list";

    private final int targetIndex;

    private final Update update;

    public EditCommand(int targetIndex, String title, Set<String> tags, String description) throws IllegalValueException {
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
        this.update = new Update(newTitle, newTags, newDescription);
    }

    @Override
    public CommandResult execute() {   
        UnmodifiableObservableList<Entry> lastShownList = model.getFilteredPersonList();
        
        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_ENTRY_DISPLAYED_INDEX);
        }

        Entry taskToEdit = lastShownList.get(targetIndex - 1);
        update.setTask(taskToEdit);
        assert model != null;
        try {
            model.editTask(update);
        } catch (PersonNotFoundException e) {
            assert false : "The target entry cannot be missing";
        } catch (DuplicateTaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, taskToEdit));
    }

}
